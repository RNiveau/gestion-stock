/**
 * 
 */
package net.blog.dev.gestion.stocks.back.services.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import net.blog.dev.gestion.stocks.back.json.HistoricQuote;
import net.blog.dev.gestion.stocks.back.json.Quote;
import net.blog.dev.gestion.stocks.back.services.api.IQuoteBService;
import net.blog.dev.gestion.stocks.back.services.api.IQuoteResource;
import net.blog.dev.gestion.stocks.back.services.api.IYahooFinanceBService;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 * @author Kiva
 * 
 */
@ApplicationScoped
public class QuoteBServiceImpl implements IQuoteBService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.blog.dev.gestion.stocks.back.services.api.IQuoteBService#getQuote
	 * (java.lang.String)
	 */
	@Override
	public Quote getQuote(String code) {
		final String json = executeYahooService(code);
		ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
		try {
			Quote quote = mapper.readValue(json, Quote.class);
			return quote;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
		return quote;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.blog.dev.gestion.stocks.back.services.api.IQuoteBService#getQuotes
	 * (java.lang.String[])
	 */
	@Override
	public List<Quote> getQuotes(String... codes) {
		final String json = executeYahooService(codes);
		ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
		try {
			List<Quote> quotes = mapper.readValue(json,
					new TypeReference<List<Quote>>() {
					});
			return quotes;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<HistoricQuote> getHistoricQuote(String code, Date start,
			Date end) {
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
			List<HistoricQuote> quotes = mapper.readValue(json,
					new TypeReference<List<HistoricQuote>>() {
					});
			return quotes;
		} catch (JsonParseException e) {
			System.err.println(query);
			e.printStackTrace();
		} catch (JsonMappingException e) {
			System.err.println(query);
			System.err.println(json);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
