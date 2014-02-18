/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import net.blog.dev.gestion.stocks.back.StockId;
import net.blog.dev.gestion.stocks.back.daos.IStockDao;
import net.blog.dev.gestion.stocks.dto.DtoDividend;
import net.blog.dev.gestion.stocks.dto.DtoStock;
import net.blog.dev.gestion.stocks.middle.CalculUtils;
import net.blog.dev.gestion.stocks.middle.Utils;
import net.blog.dev.gestion.stocks.middle.api.IDividendMService;
import net.blog.dev.gestion.stocks.middle.beans.AddDividendBean;
import net.blog.dev.gestion.stocks.middle.beans.DividendBean;

/**
 * @author Kiva
 * 
 */
@ApplicationScoped
public class DividendMServiceImpl implements IDividendMService {

	@Inject
	private IStockDao stockDao;

	@Override
	public Map<String, String> validate(AddDividendBean bean) {
		final KValidator<AddDividendBean> kValidator = new KValidator<AddDividendBean>();
		return kValidator.validate(bean);
	}

	@Override
	public void addDividend(AddDividendBean bean) {
		if (validate(bean).size() > 0) {
			throw new IllegalArgumentException("bean not valide");
		}
		final DtoStock stock = stockDao.getStock(bean.getId());
		if (stock == null) {
			throw new IllegalArgumentException("stock not found");
		}
		DtoDividend dtoDividend = new DtoDividend();
		dtoDividend.setDate(Utils.getDate(bean.getDate(), "dd/MM/yyyy"));
		final float price = Float.parseFloat(bean.getPrice());
		dtoDividend.setUnitPrice(price / stock.getQuantity());
		dtoDividend.setTotalPrice(price);
		dtoDividend.setPercentage(CalculUtils.getPercentageIntoValues(price,
				stock.getBuyOrdre().getTotalPrice()));
		stock.getDividends().add(dtoDividend);
		stockDao.saveStock(stock);
	}

	@Override
	public List<DividendBean> getDividends(StockId id) {
		final DtoStock stock = stockDao.getStock(id);
		final List<DtoDividend> dividends = stock.getDividends();
		List<DividendBean> dividendsBean = new ArrayList<>();
		if (dividends != null) {
			for (DtoDividend bean : dividends) {
				final DividendBean dividendBean = new DividendBean();
				dividendBean.setDate(Utils.formatDate(bean.getDate(),
						"dd/MM/yyyy"));
				dividendBean.setPrice(bean.getTotalPrice());
				dividendBean.setPercentage(bean.getPercentage());
				dividendsBean.add(dividendBean);
			}
		}
		return dividendsBean;
	}

}
