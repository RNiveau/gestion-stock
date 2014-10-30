/**
 *
 */
package net.blog.dev.gestion.stocks.middle.impl;

import net.blog.dev.gestion.stocks.back.daos.IStockDao;
import net.blog.dev.gestion.stocks.back.json.Quote;
import net.blog.dev.gestion.stocks.back.services.api.IQuoteBService;
import net.blog.dev.gestion.stocks.dto.DirectionEnum;
import net.blog.dev.gestion.stocks.dto.DtoStock;
import net.blog.dev.gestion.stocks.middle.CalculUtils;
import net.blog.dev.gestion.stocks.middle.api.IStocksListMService;
import net.blog.dev.gestion.stocks.middle.beans.StockListBean;
import net.blog.dev.gestion.stocks.middle.beans.StockListCloseBean;
import net.blog.dev.gestion.stocks.middle.beans.StockListRunningBean;
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

    @Override
    public List<StockListBean> getStocksListRunning() {
        final List<DtoStock> listStocks = stockDao.getListStocks();
        sortStockList(listStocks);
        List<StockListBean> listBean = new ArrayList<StockListBean>();
        for (DtoStock stock : listStocks) {
            if (stock.getSellOrder() == null)
                listBean.add(generateStockListBean(stock, false));
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

    private StockListBean generateStockListBean(final DtoStock stock,
                                                boolean closeStock) {
        StockListBean bean = null;
        if (closeStock)
            bean = new StockListCloseBean();
        else {
            bean = new StockListRunningBean();
            ((StockListRunningBean) bean).setAtr(stock.getAtr());
        }

        bean.setQuantity(stock.getQuantity());
        bean.setTitle(stock.getName());
        bean.setCode(stock.getCode());
        bean.setDirection(stock.getDirection().equals(DirectionEnum.BUY) ? "A"
                : "V");
        if (stock.getBuyOrdre() != null) {
            bean.setDate(stock.getBuyOrdre().getDate());
//            Utils.formatDate(stock.getBuyOrdre().getDate(),
//                    "dd/MM/yyyy"));
            bean.setPrice(stock.getBuyOrdre().getTotalPrice());
            bean.setUnitPrice(stock.getBuyOrdre().getUnitPrice());
            bean.setDayRunning(CalculUtils.getDayWithNow(stock.getBuyOrdre()
                    .getDate()));
        }
        if (stock.getSellOrder() != null) {
            StockListCloseBean closeBean = (StockListCloseBean) bean;
            closeBean.setSellDate(stock.getSellOrder()
                    .getDate());
            closeBean.setSellTaxes(stock.getSellOrder().getTaxes());
            closeBean.setSellPrice(stock.getSellOrder().getTotalPrice());
            closeBean.setSellUnitPrice(stock.getSellOrder().getUnitPrice());
            bean.setDayRunning(CalculUtils.getDaysBetweenDate(stock
                    .getBuyOrdre().getDate(), stock.getSellOrder().getDate()));
            if (stock.getBuyOrdre() != null) {
                closeBean.setGainPercentage(CalculUtils
                        .getPercentageBetweenTwoValues(stock.getBuyOrdre()
                                .getTotalPrice(), stock.getSellOrder()
                                .getTotalPrice()));
                // Calcul des gains selon sens d'achat
                if (DirectionEnum.BUY.equals(stock.getDirection())) {
                    closeBean.setGain(stock.getSellOrder().getTotalPrice()
                            - stock.getBuyOrdre().getTotalPrice());
                    closeBean.setGainLessTaxes(closeBean.getGain()
                            - (stock.getBuyOrdre().getTaxes() + stock
                            .getSellOrder().getTaxes()));
                    Float buyPrice = stock.getBuyOrdre().getTotalPrice()
                            + stock.getBuyOrdre().getTaxes();
                    Float sellPrice = stock.getSellOrder().getTotalPrice()
                            - stock.getSellOrder().getTaxes();
                    closeBean
                            .setGainLessTaxesPercentage(CalculUtils
                                    .getPercentageBetweenTwoValues(buyPrice,
                                            sellPrice));
                    if (stock.getBuyOrdre().getTotalPrice() > stock
                            .getSellOrder().getTotalPrice()) {
                        closeBean.setGainPercentage(closeBean
                                .getGainPercentage() * -1);
                        closeBean.setGainLessTaxesPercentage(closeBean
                                .getGainLessTaxesPercentage() * -1);
                    }
                } else if (DirectionEnum.SELL.equals(stock.getDirection())) {
                    closeBean.setGain(stock.getBuyOrdre().getTotalPrice()
                            - stock.getSellOrder().getTotalPrice());
                    closeBean.setGainLessTaxes(closeBean.getGain()
                            - (stock.getBuyOrdre().getTaxes() + stock
                            .getSellOrder().getTaxes()));

                    Float buyPrice = stock.getBuyOrdre().getTotalPrice()
                            - stock.getBuyOrdre().getTaxes();
                    Float sellPrice = stock.getSellOrder().getTotalPrice()
                            + stock.getSellOrder().getTaxes();
                    closeBean
                            .setGainLessTaxesPercentage(CalculUtils
                                    .getPercentageBetweenTwoValues(buyPrice,
                                            sellPrice));
                    if (stock.getBuyOrdre().getTotalPrice() < stock
                            .getSellOrder().getTotalPrice()) {
                        closeBean.setGainPercentage(closeBean
                                .getGainPercentage() * -1);
                        closeBean.setGainLessTaxesPercentage(closeBean
                                .getGainLessTaxesPercentage() * -1);
                    }
                }
            }

        }
        return bean;
    }

    @Override
    public List<StockListCloseBean> getStocksListClose() {
        final List<DtoStock> listStocks = stockDao.getListStocks();
        sortStockList(listStocks);
        List<StockListCloseBean> listBean = new ArrayList<StockListCloseBean>();
        for (DtoStock stock : listStocks) {
            if (stock.getSellOrder() != null)
                listBean.add((StockListCloseBean) generateStockListBean(stock,
                        true));
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
                                stockListCloseBean.setSellPrice(0f);
                                stockListCloseBean.setSellTaxes(0f);
                                stockListCloseBean.setDirection(stock.getDirection());
                                grouped.add(stockListCloseBean);
                                return stockListCloseBean;
                            });
            stockClose.setQuantity(stockClose.getQuantity() + stock.getQuantity());
            stockClose.setPrice(stockClose.getPrice() + stock.getPrice());
            stockClose.setGain(stockClose.getGain() + stock.getGain());
            stockClose.setSellPrice(stockClose.getSellPrice() + stock.getSellPrice());
            stockClose.setSellTaxes(stockClose.getSellTaxes() + stock.getSellTaxes());
        });
        grouped.stream().forEach(stock -> {
            stock.setUnitPrice(stock.getPrice() / stock.getQuantity());
            stock.setSellUnitPrice(stock.getSellPrice() / stock.getQuantity());
            stock.setGainPercentage(CalculUtils.getPercentageBetweenTwoValues(stock.getPrice(), stock.getSellPrice()));
            if (stock.getDirection().equals(DirectionEnum.BUY.value())) {
                if (stock.getPrice() < stock.getSellPrice())
                    stock.setGainPercentage(stock.getGainPercentage() * -1);
            } else {
                if (stock.getPrice() > stock.getSellPrice())
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
