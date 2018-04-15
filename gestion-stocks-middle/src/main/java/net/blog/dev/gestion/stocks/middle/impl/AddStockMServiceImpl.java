/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.impl;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import net.blog.dev.gestion.stocks.back.daos.IStockDao;
import net.blog.dev.gestion.stocks.dto.AccountEnum;
import net.blog.dev.gestion.stocks.dto.DirectionEnum;
import net.blog.dev.gestion.stocks.dto.DtoOrder;
import net.blog.dev.gestion.stocks.dto.DtoStock;
import net.blog.dev.gestion.stocks.dto.OrdreTypeEnum;
import net.blog.dev.gestion.stocks.dto.StrategyEnum;
import net.blog.dev.gestion.stocks.middle.Utils;
import net.blog.dev.gestion.stocks.middle.api.IAddStockMService;
import net.blog.dev.gestion.stocks.middle.beans.AddStockBean;

import org.apache.commons.lang3.EnumUtils;

/**
 * @author Kiva
 * 
 */
@ApplicationScoped
public class AddStockMServiceImpl extends AbstractMService implements
		IAddStockMService {

	@Inject
	private IStockDao stockDao;

	@Override
	public void addStock(AddStockBean bean) {
		if (validate(bean).size() > 0) {
			throw new IllegalArgumentException("bean not valide");
		}
		DtoStock stock = new DtoStock();
		stock.setAccount(EnumUtils.getEnum(AccountEnum.class, bean.getAccount()));
		stock.setAtr(Utils.getFloat(bean.getAtr()));
		stock.setCode(bean.getCode());
		stock.setDirection(EnumUtils.getEnum(DirectionEnum.class,
				bean.getDirection()));
		stock.setName(bean.getName());
		stock.setQuantity(Utils.getFloat(bean.getQuantity()));
		stock.setStrategy(EnumUtils.getEnum(StrategyEnum.class,
				bean.getStrategy()));
		stock.setType(EnumUtils.getEnum(OrdreTypeEnum.class,
				bean.getTypeOrder()));

		DtoOrder order = new DtoOrder();
		order.setDate(Utils.getDate(bean.getDate(), "dd/MM/yyyy"));
		order.setTaxes(Utils.getFloat(bean.getTaxes()));
		order.setTotalPrice(Utils.getFloat(bean.getPrice()));
		if (order.getTotalPrice() > 0 && stock.getQuantity() > 0)
			order.setUnitPrice(order.getTotalPrice() / stock.getQuantity());
		stock.setBuyOrdre(order);
		stockDao.saveStock(stock);

	}

	@Override
	public Map<String, String> validate(AddStockBean bean) {
		final KValidator<AddStockBean> kValidator = new KValidator<AddStockBean>();
		return kValidator.validate(bean);
	}

	@Override
	public List<String> getListStrategies() {
		return getListEnum(StrategyEnum.values());
	}

	@Override
	public List<String> getListAccount() {
		return getListEnum(AccountEnum.values());
	}

	@Override
	public List<String> getListDirection() {
		return getListEnum(DirectionEnum.values());
	}

	@Override
	public List<String> getListOrderType() {
		return getListEnum(OrdreTypeEnum.values());
	}
}
