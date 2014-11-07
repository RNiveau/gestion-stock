package net.blog.dev.gestion.stocks.middle.helpers.impl;

import net.blog.dev.gestion.stocks.dto.DirectionEnum;
import net.blog.dev.gestion.stocks.dto.DtoStock;
import net.blog.dev.gestion.stocks.middle.CalculUtils;
import net.blog.dev.gestion.stocks.middle.beans.StockListBean;
import net.blog.dev.gestion.stocks.middle.beans.StockListCloseBean;
import net.blog.dev.gestion.stocks.middle.beans.StockListRunningBean;
import net.blog.dev.gestion.stocks.middle.helpers.api.IStockHelper;

/**
 * Created by romainn on 03/11/2014.
 */
public class StockHelperImpl implements IStockHelper {

    @Override
    public StockListBean stockDtoToBean(DtoStock stock) {
        StockListBean bean = null;
        if (stock.getSellOrder() != null)
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
}
