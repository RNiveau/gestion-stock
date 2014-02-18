/**
 * 
 */
package net.blog.dev.gestion.stocks.back.daos;

import java.util.List;

import net.blog.dev.gestion.stocks.back.StockId;
import net.blog.dev.gestion.stocks.dto.DtoStock;

/**
 * @author Kiva
 * 
 */
public interface IStockDao {

	List<DtoStock> getListStocks();

	void saveStock(DtoStock stock);

	DtoStock getStock(StockId id);
}
