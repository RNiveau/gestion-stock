/**
 *
 */
package net.blog.dev.gestion.stocks.back.services.impl;

import net.blog.dev.gestion.stocks.back.KContext;
import net.blog.dev.gestion.stocks.back.json.HistoricQuote;
import net.blog.dev.gestion.stocks.back.json.Quote;
import net.blog.dev.gestion.stocks.back.services.api.IAlphaAvantageBService;
import net.blog.dev.gestion.stocks.back.services.api.IQuoteBService;
import net.blog.dev.gestion.stocks.back.services.api.IQuoteResource;
import net.blog.dev.gestion.stocks.back.services.api.IYahooFinanceBService;
import net.blog.dev.gestion.stocks.back.services.beans.AlphaAvantageWrapper;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Kiva
 */
@ApplicationScoped
public class QuoteBServiceImpl implements IQuoteBService {

    static final Logger logger = LoggerFactory.getLogger(QuoteBServiceImpl.class);

    private Cache cache;

    @Inject
    private KContext context;


    @PostConstruct
    private void init() {
        logger.debug("QuoteBServiceImpl init");
        cache = CacheManager.getInstance().getCache("cache");
        if (cache != null)
            logger.debug("Cache present and initialized");
    }

    /*
     * (non-Javadoc)
	 * 
	 * @see
	 * net.blog.dev.gestion.stocks.back.services.api.IQuoteBService#getQuote
	 * (java.lang.String)
	 */
    @Override
    public Quote getQuote(String code) {
        Element element = cache.get(code);
        if (element != null) {
            logger.info("getQuote found in cache => {}", code);
            return (Quote) element.getObjectValue();
        }
        logger.info("getQuote not in cache => {}", code);
        Date timer = Instant.now().toDate();
        final String json = executeAlphaService(code);
        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
        try {
            AlphaAvantageWrapper alphaAvantageWrapper = mapper.readValue(json, AlphaAvantageWrapper.class);
            Quote quote = new Quote();
            quote.setLastTradePriceOnly(alphaAvantageWrapper.getQuotes().get(0).getClose());
            quote.setSymbol(code);
            Date timer2 = Instant.now().toDate();
            logger.debug("Time in method: {}", new Duration(timer.getTime(), timer2.getTime()).getMillis());
            return quote;
        } catch (IOException e) {
            Date timer2 = Instant.now().toDate();
            logger.debug("Time in method: {}", new Duration(timer.getTime(), timer2.getTime()).getMillis());
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * @return
     */
    private String executeYahooService(String... codes) {
        final IYahooFinanceBService factory = getYahooService();
        String inCondition = "";
        for (String code : codes) {
            if (!"".equals(inCondition))
                inCondition = inCondition + ",";
            inCondition = inCondition + "%22" + code + ".PA%22";
        }
        final IQuoteResource quoteResource = factory.getQuote(
                "select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20("
                        + inCondition + ")", "json",
                "store%3A%2F%2Fdatatables.org%2Falltableswithkeys", "");

        String quote = quoteResource.getQuote();
        quote = quote.replaceAll("^.*\\\"quote\\\":", "");
        quote = quote.replaceAll("}}$", "");
        logger.debug(quote);
        return quote;
    }

    private String executeAlphaService(String... codes) {
        IAlphaAvantageBService factory = getAlphaAvantageBService();
        return factory.getQuote("TIME_SERIES_DAILY", codes[0]+".PA", context.getConfiguration().getAlphaAvantageApiKey()).getQuote().toString();
    }

    /**
     * @return
     */
    private IYahooFinanceBService getYahooService() {
        final IYahooFinanceBService factory = JAXRSClientFactory.create(
                "http://query.yahooapis.com/v1/public/",
                IYahooFinanceBService.class);
        return factory;
    }

    private IAlphaAvantageBService getAlphaAvantageBService() {
        final IAlphaAvantageBService factory = JAXRSClientFactory.create(
                "https://www.alphavantage.co",
                IAlphaAvantageBService.class);
        return factory;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * net.blog.dev.gestion.stocks.back.services.api.IQuoteBService#getQuotes
     * (java.lang.String[])
     */
    @Override
    public List<Quote> getQuotes(String... codes) {
        logger.info("getQuotes {}", codes);
        final String json = executeYahooService(codes);
        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
        try {
            List<Quote> quotes = mapper.readValue(json,
                    new TypeReference<List<Quote>>() {
                    });
            return quotes;
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public List<HistoricQuote> getHistoricQuote(String code, Date start,
                                                Date end) {
        logger.info("getHistoricQuote {}, {}, {}", code, start, end);
        Date timer = Instant.now().toDate();
        final IYahooFinanceBService yahooService = getYahooService();
        final String query = "select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22"
                + code
                + ".PA%22%20and%20startDate%20%3D%20%22"
                + new SimpleDateFormat("yyyy-MM-dd").format(start)
                + "%22%20and%20endDate%20%3D%20%22"
                + new SimpleDateFormat("yyyy-MM-dd").format(end) + "%22";
        final IQuoteResource quoteResource = yahooService.getQuote(query,
                "json", "store%3A%2F%2Fdatatables.org%2Falltableswithkeys", "");

        String json = quoteResource.getQuote();
        json = json.replaceAll("^.*\\\"quote\\\":", "");
        json = json.replaceAll("}}$", "");
        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
        try {
            mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
            List<HistoricQuote> quotes = mapper.readValue(json,
                    new TypeReference<List<HistoricQuote>>() {
                    });
            Date timer2 = Instant.now().toDate();
            logger.debug("Time in method: {}", new Duration(timer.getTime(), timer2.getTime()).getMillis());
            return quotes;
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return null;
    }
}
