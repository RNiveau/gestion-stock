/**
 * 
 */
package net.blog.dev.gestion.stocks.back.daos;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import net.blog.dev.gestion.stocks.back.StockId;
import net.blog.dev.gestion.stocks.back.manager.IConfigurationManager;
import net.blog.dev.gestion.stocks.dto.DtoStock;

/**
 * @author Kiva
 * 
 */
@ApplicationScoped
public class StockDaoImpl implements IStockDao {

	@Inject
	private IConfigurationManager manager;

	@Override
	public List<DtoStock> getListStocks() {
		return manager.getPortfolio().getStocks();
	}

	@Override
	public void saveStock(DtoStock stock) {
		final List<DtoStock> stocks = manager.getPortfolio().getStocks();
		// on parcours la liste, et on supprime si un existe deja
		for (DtoStock s : stocks) {
			if (generateId(s).equals(generateId(stock))) {
				stocks.remove(s);
				break;
			}
		}
		stocks.add(stock);
		manager.save();
	}

	@Override
	public DtoStock getStock(StockId id) {
		final List<DtoStock> stocks = manager.getPortfolio().getStocks();
		for (DtoStock stock : stocks) {
			if (generateId(stock).equals(id))
				return stock;
		}
		return null;
	}

	private StockId generateId(DtoStock stock) {
		StockId id = new StockId();
		id.setCode(stock.getCode());
		if (stock.getBuyOrdre() != null) {
			id.setUnitPrice(stock.getBuyOrdre().getUnitPrice());
			id.setBuyDate(stock.getBuyOrdre().getDate());
		}
		return id;
	}
}
