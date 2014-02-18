/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import net.blog.dev.gestion.stocks.back.StockId;
import net.blog.dev.gestion.stocks.back.daos.IStockDao;
import net.blog.dev.gestion.stocks.dto.DtoOrder;
import net.blog.dev.gestion.stocks.dto.DtoStock;
import net.blog.dev.gestion.stocks.dto.OrdreTypeEnum;
import net.blog.dev.gestion.stocks.middle.CalculUtils;
import net.blog.dev.gestion.stocks.middle.Utils;
import net.blog.dev.gestion.stocks.middle.api.IConfigurationMSservice;
import net.blog.dev.gestion.stocks.middle.api.IDetailStockMService;
import net.blog.dev.gestion.stocks.middle.beans.DetailStockBean;
import net.blog.dev.gestion.stocks.middle.beans.StockListBean;

/**
 * @author Kiva
 * 
 */
@ApplicationScoped
public class DetailStockMServiceImpl implements IDetailStockMService {

	@Inject
	private IStockDao stockDao;

	@Inject
	private IConfigurationMSservice configurationMSservice;

	@Override
	public DetailStockBean getDetailStockBean(StockListBean stockListBean) {
		DetailStockBean detailStockBean = new DetailStockBean();
		final StockId id = generateId(stockListBean);
		detailStockBean.setId(id);
		final DtoStock stock = stockDao.getStock(id);
		if (stock != null) {
			detailStockBean.setAccount(stock.getAccount().toString());
			detailStockBean.setStrategy(stock.getStrategy().toString());
			detailStockBean.setDirection(stock.getDirection().toString());
			detailStockBean.setType(stock.getType().toString());
			if (stock.getBuyOrdre() != null) {
				detailStockBean.setTaxes(stock.getBuyOrdre().getTaxes());
				final float unitPrice = stock.getBuyOrdre().getUnitPrice();
				final Float atr = stock.getAtr();
				if (OrdreTypeEnum.SRD.equals(stock.getType())) {
					final Integer dayWithNow = CalculUtils.getDayWithNow(stock
							.getBuyOrdre().getDate());
					final Float srdLoan = configurationMSservice
							.getConfiguration().getSrdLoan();
					detailStockBean.setEstimateSrd(stock.getBuyOrdre()
							.getTotalPrice() * (dayWithNow * (srdLoan / 100f)));
				}
				if (atr != null) {
					detailStockBean.addStopGain(unitPrice + atr, CalculUtils
							.getPercentageBetweenTwoValues(unitPrice, unitPrice
									+ atr), DetailStockBean.GAIN_ATR_1);
					detailStockBean.addStopGain(unitPrice + (1.5f * atr),
							CalculUtils.getPercentageBetweenTwoValues(
									unitPrice, unitPrice + (1.5f * atr)),
							DetailStockBean.GAIN_ATR_15);
					detailStockBean.addStopGain(unitPrice + (2f * atr),
							CalculUtils.getPercentageBetweenTwoValues(
									unitPrice, unitPrice + (2 * atr)),
							DetailStockBean.GAIN_ATR_2);
					detailStockBean.addStopGain(unitPrice - atr, CalculUtils
							.getPercentageBetweenTwoValues(unitPrice, unitPrice
									- atr), DetailStockBean.STOP_ATR_1);
					detailStockBean.addStopGain(unitPrice - (1.5f * atr),
							CalculUtils.getPercentageBetweenTwoValues(
									unitPrice, unitPrice - (1.5f * atr)),
							DetailStockBean.STOP_ATR_15);
					detailStockBean.addStopGain(unitPrice - (2f * atr),
							CalculUtils.getPercentageBetweenTwoValues(
									unitPrice, unitPrice - (2f * atr)),
							DetailStockBean.STOP_ATR_2);
				}
			}
			if (stock.getSellOrder() != null) {
				detailStockBean.setTaxesClose(stock.getSellOrder().getTaxes());
			}
		}
		return detailStockBean;
	}

	@Override
	public void updateAtr(StockListBean stockListBean, Float atr) {
		final DtoStock stock = stockDao.getStock(generateId(stockListBean));
		stock.setAtr(atr);
		stockDao.saveStock(stock);
	}

	@Override
	public void updateQuantity(StockListBean stockListBean, Integer quantity) {
		if (quantity != null && quantity > 0) {
			final DtoStock stock = stockDao.getStock(generateId(stockListBean));
			stock.setQuantity(quantity);
			final DtoOrder buyOrdre = stock.getBuyOrdre();
			// mise a jour du prix unitaire
			if (buyOrdre != null) {
				if (buyOrdre.getTotalPrice() > 0) {
					buyOrdre.setUnitPrice(buyOrdre.getTotalPrice() / quantity);
					stockListBean.setUnitPrice(buyOrdre.getUnitPrice());
				}
			}
			stockDao.saveStock(stock);
		}
	}

	private StockId generateId(StockListBean stockListBean) {
		StockId id = new StockId();
		id.setBuyDate(Utils.getDate(stockListBean.getDate(), "dd/MM/yyyy"));
		id.setCode(stockListBean.getCode());
		id.setUnitPrice(stockListBean.getUnitPrice());
		return id;
	}
}
