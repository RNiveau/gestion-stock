/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.impl;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import net.blog.dev.gestion.stocks.back.daos.IStockDao;
import net.blog.dev.gestion.stocks.dto.DirectionEnum;
import net.blog.dev.gestion.stocks.dto.DtoStock;
import net.blog.dev.gestion.stocks.middle.CalculUtils;
import net.blog.dev.gestion.stocks.middle.Utils;
import net.blog.dev.gestion.stocks.middle.api.IStocksListMService;
import net.blog.dev.gestion.stocks.middle.beans.StockListBean;
import net.blog.dev.gestion.stocks.middle.beans.StockListCloseBean;
import net.blog.dev.gestion.stocks.middle.beans.StockListRunningBean;

/**
 * @author Kiva
 * 
 */
@ApplicationScoped
public class StocksListMServiceImpl extends AbstractMService implements
		IStocksListMService {

	@Inject
	private IStockDao stockDao;

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

	private StockListBean generateStockListBean(DtoStock stock,
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
			bean.setDate(Utils.formatDate(stock.getBuyOrdre().getDate(),
					"dd/MM/yyyy"));
			bean.setPrice(stock.getBuyOrdre().getTotalPrice());
			bean.setUnitPrice(stock.getBuyOrdre().getUnitPrice());
			bean.setDayRunning(CalculUtils.getDayWithNow(stock.getBuyOrdre()
					.getDate()));
		}
		if (stock.getSellOrder() != null) {
			StockListCloseBean closeBean = (StockListCloseBean) bean;
			closeBean.setSellDate(Utils.formatDate(stock.getSellOrder()
					.getDate(), "dd/MM/yyyy"));
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

}
