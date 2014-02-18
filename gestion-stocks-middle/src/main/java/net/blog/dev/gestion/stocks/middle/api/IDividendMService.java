/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.api;

import java.util.List;
import java.util.Map;

import net.blog.dev.gestion.stocks.back.StockId;
import net.blog.dev.gestion.stocks.middle.beans.AddDividendBean;
import net.blog.dev.gestion.stocks.middle.beans.DividendBean;

/**
 * @author Kiva
 * 
 */
public interface IDividendMService {

	public void addDividend(AddDividendBean bean);

	public Map<String, String> validate(AddDividendBean bean);

	public List<DividendBean> getDividends(StockId id);
}
