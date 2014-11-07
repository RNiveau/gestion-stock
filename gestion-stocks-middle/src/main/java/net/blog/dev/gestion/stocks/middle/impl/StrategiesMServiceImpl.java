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
        listStocks.stream().filter(stock -> stock.getStrategy().value().equals(strategy) && stock.getSellOrder() != null)
                .forEach(stock -> {
                    StockListCloseBean stockListBean = (StockListCloseBean) stockHelper.stockDtoToBean(stock);
                    if (stock.getDirection().equals(DirectionEnum.BUY)) {
                        strategyBean.setNbrBuy(strategyBean.getNbrBuy() + 1);
                        strategyBean.setBenefitsBuy(strategyBean.getBenefitsBuy() + stockListBean.getGain());
                    }
               });
        return strategyBean;
    }
}
