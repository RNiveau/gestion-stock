/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.api;

import java.util.List;
import java.util.Map;

import net.blog.dev.gestion.stocks.middle.beans.AddStockBean;

/**
 * @author Kiva
 * 
 */
public interface IAddStockMService {

	public void addStock(AddStockBean bean);

	public Map<String, String> validate(AddStockBean bean);

	public List<String> getListStrategies();

	public List<String> getListAccount();

	public List<String> getListDirection();

	public List<String> getListOrderType();

}
