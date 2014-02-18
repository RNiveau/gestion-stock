/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.impl;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import net.blog.dev.gestion.stocks.back.daos.IStockDao;
import net.blog.dev.gestion.stocks.dto.DtoOrder;
import net.blog.dev.gestion.stocks.dto.DtoStock;
import net.blog.dev.gestion.stocks.middle.Utils;
import net.blog.dev.gestion.stocks.middle.api.ICloseStockMService;
import net.blog.dev.gestion.stocks.middle.beans.CloseBean;

/**
 * @author Kiva
 * 
 */
@ApplicationScoped
public class CloseStockMServiceImpl implements ICloseStockMService {

	@Inject
	private IStockDao stockDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.blog.dev.gestion.stocks.middle.api.ICloseStockMService#closeStock
	 * (net.blog.dev.gestion.stocks.middle.beans.CloseBean)
	 */
	@Override
	public void closeStock(CloseBean bean) {
		if (validate(bean).size() > 0) {
			throw new IllegalArgumentException("bean not valide");
		}
		final DtoStock stock = stockDao.getStock(bean.getId());
		if (stock == null) {
			throw new IllegalArgumentException("stock not found");
		}
		DtoOrder sellOrder = new DtoOrder();
		sellOrder.setDate(Utils.getDate(bean.getDate(), "dd/MM/yyyy"));
		sellOrder.setTaxes(Utils.getFloat(bean.getTaxes()));
		sellOrder.setTotalPrice(Utils.getFloat(bean.getPrice()));
		if (sellOrder.getTotalPrice() > 0 && stock.getQuantity() > 0)
			sellOrder.setUnitPrice(sellOrder.getTotalPrice()
					/ stock.getQuantity());
		stock.setSellOrder(sellOrder);
		stockDao.saveStock(stock);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.blog.dev.gestion.stocks.middle.api.ICloseStockMService#validate(net
	 * .blog.dev.gestion.stocks.middle.beans.CloseBean)
	 */
	@Override
	public Map<String, String> validate(CloseBean bean) {
		final KValidator<CloseBean> kValidator = new KValidator<CloseBean>();
		return kValidator.validate(bean);
	}
}
