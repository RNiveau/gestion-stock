/**
 *
 */
package net.blog.dev.gestion.stocks.middle.impl;

import net.blog.dev.gestion.stocks.back.daos.IStockDao;
import net.blog.dev.gestion.stocks.back.json.Quote;
import net.blog.dev.gestion.stocks.back.services.api.IQuoteBService;
import net.blog.dev.gestion.stocks.dto.DtoStock;
import net.blog.dev.gestion.stocks.middle.CalculUtils;
import net.blog.dev.gestion.stocks.middle.api.IStocksListMService;
import net.blog.dev.gestion.stocks.middle.beans.StockListBean;
import net.blog.dev.gestion.stocks.middle.beans.StockListCloseBean;
import net.blog.dev.gestion.stocks.middle.beans.StockListRunningBean;
import net.blog.dev.gestion.stocks.middle.helpers.api.IStockHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kiva
 */
@ApplicationScoped
public class StocksListMServiceImpl extends AbstractMService implements
        IStocksListMService {

    static final Logger logger = LoggerFactory.getLogger(StocksListMServiceImpl.class);

    @Inject
    private IStockDao stockDao;

    @Inject
    private IQuoteBService quoteBService;

    @Inject
    private IStockHelper stockHelper;

    @Override
    public List<StockListBean> getStocksListRunning() {
        final List<DtoStock> listStocks = stockDao.getListStocks();
        sortStockList(listStocks);
        List<StockListBean> listBean = new ArrayList<StockListBean>();
        for (DtoStock stock : listStocks) {
            if (stock.getSellOrder() == null)
                listBean.add(stockHelper.stockDtoToBean(stock));
        }
        return listBean;
    }

    @Override
    public List<StockListBean> getStocksListRunning(boolean group) {
        List<StockListBean> stocksListRunning = getStocksListRunning();
        if (!group)
            return stocksListRunning;

        List<StockListBean> grouped = new ArrayList<>();
        stocksListRunning.stream().forEach(stock -> {
            StockListBean stockRunning =
                    grouped.stream()
                            .filter((element) -> element.getCode().equals(stock.getCode()) && element.getDirection().equals(stock.getDirection()))
                            .findFirst()
                            .orElseGet(() -> {
                                StockListRunningBean stockListRunningBean = new StockListRunningBean();
                                stockListRunningBean.setCode(stock.getCode());
                                stockListRunningBean.setTitle(stock.getTitle());
                                stockListRunningBean.setQuantity(0);
                                stockListRunningBean.setPrice(0f);
                                stockListRunningBean.setDirection(stock.getDirection());
                                grouped.add(stockListRunningBean);
                                return stockListRunningBean;
                            });
            stockRunning.setQuantity(stockRunning.getQuantity() + stock.getQuantity());
            stockRunning.setPrice(stockRunning.getPrice() + stock.getPrice());
        });
        grouped.stream().forEach(stock -> stock.setUnitPrice(stock.getPrice() / stock.getQuantity()));
        return grouped;
    }


    @Override
    public List<StockListCloseBean> getStocksListClose() {
        final List<DtoStock> listStocks = stockDao.getListStocks();
        sortStockList(listStocks);
        List<StockListCloseBean> listBean = new ArrayList<StockListCloseBean>();
        for (DtoStock stock : listStocks) {
            if (stock.getSellOrder() != null)
                listBean.add((StockListCloseBean) stockHelper.stockDtoToBean(stock));
        }
        return listBean;
    }

    @Override
    public List<StockListCloseBean> getStocksListClose(boolean group) {
        List<StockListCloseBean> stocksListClose = getStocksListClose();
        if (!group)
            return stocksListClose;

        List<StockListCloseBean> grouped = new ArrayList<>();
        stocksListClose.stream().forEach(stock -> {
            StockListCloseBean stockClose =
                    grouped.stream()
                            .filter((element) -> element.getCode().equals(stock.getCode()) && element.getDirection().equals(stock.getDirection()))
                            .findFirst()
                            .orElseGet(() -> {
                                StockListCloseBean stockListCloseBean = new StockListCloseBean();
                                stockListCloseBean.setCode(stock.getCode());
                                stockListCloseBean.setTitle(stock.getTitle());
                                stockListCloseBean.setQuantity(0);
                                stockListCloseBean.setPrice(0f);
                                stockListCloseBean.setGain(0f);
                                stockListCloseBean.setGainLessTaxes(0f);
                                stockListCloseBean.setGainLessTaxesPercentage(0f);
                                stockListCloseBean.setGainPercentage(0f);
                                stockListCloseBean.setSellPrice(0f);
                                stockListCloseBean.setSellTaxes(0f);
                                stockListCloseBean.setDirection(stock.getDirection());
                                grouped.add(stockListCloseBean);
                                return stockListCloseBean;
                            });
            stockClose.setQuantity(stockClose.getQuantity() + stock.getQuantity());
            stockClose.setPrice(stockClose.getPrice() + stock.getPrice());
            stockClose.setGain(stockClose.getGain() + stock.getGain());
            stockClose.setGainLessTaxes(stockClose.getGainLessTaxes() + stock.getGainLessTaxes());
            stockClose.setGainLessTaxesPercentage(stockClose.getGainLessTaxesPercentage() + stock.getGainLessTaxesPercentage());
            stockClose.setGainPercentage(stockClose.getGainPercentage() + stock.getGainPercentage());
            stockClose.setSellPrice(stockClose.getSellPrice() + stock.getSellPrice());
            stockClose.setSellTaxes(stockClose.getSellTaxes() + stock.getSellTaxes());
        });
        grouped.stream().forEach(stock -> {
            stock.setUnitPrice(stock.getPrice() / stock.getQuantity());
            stock.setSellUnitPrice(stock.getSellPrice() / stock.getQuantity());
            stock.setGainPercentage(CalculUtils.getPercentageBetweenTwoValues(stock.getPrice(), stock.getSellPrice()));
            if (stock.getDirection().equals("A")) {
                if (stock.getPrice() > stock.getSellPrice())
                    stock.setGainPercentage(stock.getGainPercentage() * -1);
            } else {
                if (stock.getPrice() < stock.getSellPrice())
                    stock.setGainPercentage(stock.getGainPercentage() * -1);
            }
        });
        return grouped;
    }

    @Override
    public Float getActualPrice(String code) {
        logger.info("getActualPrice {}", code);
        Quote quote = quoteBService.getQuote(code);
        if (quote != null) {
            logger.debug("get {}", quote.getLastTradePriceOnly());
            return quote.getLastTradePriceOnly();
        }
        return null;
    }

}
