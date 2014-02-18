/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.api;

import java.util.Map;

import net.blog.dev.gestion.stocks.middle.beans.CloseBean;

/**
 * @author Kiva
 * 
 */
public interface ICloseStockMService {

	public void closeStock(CloseBean bean);

	public Map<String, String> validate(CloseBean bean);
}
