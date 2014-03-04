/**
 *
 */
package net.blog.dev.gestion.stocks.middle.impl;

import net.blog.dev.gestion.stocks.back.json.HistoricQuote;
import net.blog.dev.gestion.stocks.back.services.api.IQuoteBService;
import net.blog.dev.gestion.stocks.middle.CalculUtils;
import net.blog.dev.gestion.stocks.middle.api.ICompareStockMService;
import net.blog.dev.gestion.stocks.middle.beans.CompareStockBean;
import net.blog.dev.gestion.stocks.middle.beans.CompareStockResultBean;
import net.blog.dev.gestion.stocks.middle.beans.CompareStockValueBean;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Kiva
 */
@ApplicationScoped
public class CompareStockMServiceImpl extends AbstractMService implements
        ICompareStockMService {

    static final Logger logger = LoggerFactory.getLogger(CompareStockMServiceImpl.class);

    @Inject
    private IQuoteBService quoteBService;

    @Override
    public List<CompareStockResultBean> getListCompareStock(
            CompareStockBean compareStockBean) {
        logger.info("getListCompareStock");
        CacheManager cacheManager = CacheManager.getInstance();
        Cache cache = cacheManager.getCache("cache");
        List<CompareStockResultBean> list = new ArrayList<>();
        Integer duration = 1;
        if (compareStockBean != null && compareStockBean.getDuration() != null) {
            logger.debug("duration = {}, code = {}", compareStockBean.getDuration(), compareStockBean.getListCode());
            duration = compareStockBean.getDuration();
        }
        Date start = Instant.now().toDate();
        Date end = new DateTime(start.getTime()).minusMonths(duration).toDate();

        if (compareStockBean != null
                && CollectionUtils.isNotEmpty(compareStockBean.getListCode())) {
            for (String code : compareStockBean.getListCode()) {
                List<HistoricQuote> historicQuote = null;
                String keyCacheListCompareStock = getKeyCacheListCompareStock(code, start, end);
                Element element = cache.get(keyCacheListCompareStock);
                if (element != null) {
                    historicQuote = (List<HistoricQuote>) element.getObjectValue();
                    logger.debug("In cache => {}", code);
                } else {
                    logger.debug("Not in cache => {}", code);
                    historicQuote = quoteBService
                            .getHistoricQuote(code, end, start);
                    if (historicQuote != null) {
                        logger.debug("Add in cache => {}", code);
                        cache.put(new Element(keyCacheListCompareStock, historicQuote));
                    }
                }
                analyseQuotes(list, code, historicQuote);
            }
        }
        return list;
    }

    private String getKeyCacheListCompareStock(String code, Date start, Date end) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return new StringBuilder(code).append(dateFormat.format(start)).append(dateFormat.format(end)).toString();
    }

    /**
     * @param list
     * @param code
     * @param historicQuote
     */
    private void analyseQuotes(List<CompareStockResultBean> list, String code,
                               final List<HistoricQuote> historicQuote) {
        if (CollectionUtils.isNotEmpty(historicQuote)) {
            Collections.sort(historicQuote, new Comparator<HistoricQuote>() {

                @Override
                public int compare(HistoricQuote o1, HistoricQuote o2) {
                    if (o1 == null || o2 == null || o1.getDate() == null
                            || o2.getDate() == null)
                        return 0;
                    return o1.getDate().compareTo(o2.getDate());
                }

            });
            CompareStockResultBean compareStockResultBean = new CompareStockResultBean();
            compareStockResultBean.setName(code);
            Float initialPrice = 0f;
            Float rate = 0f;
            for (HistoricQuote quote : historicQuote) {
                if (initialPrice == 0f)
                    initialPrice = quote.getClose();
                else {
                    final Float percentage = CalculUtils
                            .getPercentageBetweenTwoValues(initialPrice,
                                    quote.getClose());
                    rate += initialPrice > quote.getClose() ? -percentage
                            : percentage;
                    initialPrice = quote.getClose();
                }
                final CompareStockValueBean compareStockValueBean = new CompareStockValueBean();
                compareStockValueBean.setDate(quote.getDate());
                compareStockValueBean.setValue(rate);
                compareStockResultBean.addValue(compareStockValueBean);
            }
            // Calcul lower et higher
            Float lower = 0f;
            Float higher = 0f;
            for (CompareStockValueBean compareStockValueBean : compareStockResultBean
                    .getListValue()) {
                if (compareStockValueBean.getValue() > higher)
                    higher = compareStockValueBean.getValue();
                if (compareStockValueBean.getValue() < lower)
                    lower = compareStockValueBean.getValue();
            }
            compareStockResultBean.setHigher(higher);
            compareStockResultBean.setLower(lower);
            list.add(compareStockResultBean);
        }
    }
}
