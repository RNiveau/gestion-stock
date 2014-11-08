package net.blog.dev.gestion.stocks.middle.impl;

import net.blog.dev.gestion.stocks.back.daos.IStockDao;
import net.blog.dev.gestion.stocks.dto.DirectionEnum;
import net.blog.dev.gestion.stocks.dto.DtoStock;
import net.blog.dev.gestion.stocks.middle.api.IStrategiesMService;
import net.blog.dev.gestion.stocks.middle.beans.StockListCloseBean;
import net.blog.dev.gestion.stocks.middle.beans.StrategyBean;
import net.blog.dev.gestion.stocks.middle.helpers.api.IStockHelper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Duration;
import java.time.ZoneId;
import java.util.List;

/**
 * Created by romainn on 31/10/2014.
 */
@ApplicationScoped
public class StrategiesMServiceImpl implements IStrategiesMService {

    @Inject
    private IStockDao stockDao;

    @Inject
    private IStockHelper stockHelper;

    @Override
    public StrategyBean getStrategy(String strategy) {
        final StrategyBean strategyBean = new StrategyBean();
        strategyBean.setStrategy(strategy);

        List<DtoStock> listStocks = stockDao.getListStocks();

        // we take only close order
        listStocks.stream().filter(stock -> stock.getStrategy().name().equals(strategy) && stock.getSellOrder() != null)
                .forEach(stock -> {
                    StockListCloseBean stockListBean = (StockListCloseBean) stockHelper.stockDtoToBean(stock);
                    if (stock.getDirection().equals(DirectionEnum.BUY)) {
                        strategyBean.setNbrBuy(strategyBean.getNbrBuy() + 1);
                        strategyBean.setBenefitsBuy(strategyBean.getBenefitsBuy() + stockListBean.getGain());
                        strategyBean.setDividends((float) (strategyBean.getDividends() + stock.getDividends().stream().mapToDouble(dividend -> dividend.getTotalPrice()).sum()));
                        strategyBean.setDurationBuy(strategyBean.getDurationBuy().plus(
                                Duration.between(
                                        stockListBean.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                                        stockListBean.getSellDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())));
                    } else {
                        strategyBean.setNbrSell(strategyBean.getNbrSell() + 1);
                        strategyBean.setBenefitsSell(strategyBean.getBenefitsSell() + stockListBean.getGain());
                        strategyBean.setDurationSell(strategyBean.getDurationSell().plus(
                                Duration.between(
                                        stockListBean.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                                        stockListBean.getSellDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())));
                    }
                });
        listStocks.stream().filter(stock -> stock.getStrategy().value().equals(strategy) && stock.getSellOrder() == null)
                .forEach(stock -> {
                    strategyBean.setPositionRunning(strategyBean.getPositionRunning() + 1);
                    strategyBean.setMoneyRunning(strategyBean.getMoneyRunning() + stock.getBuyOrdre().getTotalPrice());
                });
        if (strategyBean.getNbrBuy() > 0) {
            strategyBean.setBenefitsAverageBuy(strategyBean.getBenefitsBuy() / strategyBean.getNbrBuy());
            strategyBean.setDurationBuy(Duration.ofDays(strategyBean.getDurationBuy().toDays() / strategyBean.getNbrBuy()));
        }
        if (strategyBean.getNbrSell() > 0) {
            strategyBean.setBenefitsAverageSell(strategyBean.getBenefitsSell() / strategyBean.getNbrSell());
            strategyBean.setDurationSell(Duration.ofDays(strategyBean.getDurationSell().toDays() / strategyBean.getNbrSell()));
        }

        return strategyBean;
    }
}
