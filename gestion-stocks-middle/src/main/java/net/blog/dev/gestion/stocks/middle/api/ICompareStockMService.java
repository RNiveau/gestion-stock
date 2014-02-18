/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.api;

import java.util.List;

import net.blog.dev.gestion.stocks.middle.beans.CompareStockBean;
import net.blog.dev.gestion.stocks.middle.beans.CompareStockResultBean;

/**
 * @author Kiva
 * 
 */
public interface ICompareStockMService {

	public List<CompareStockResultBean> getListCompareStock(
			CompareStockBean compareStockBean);

}
