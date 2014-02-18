/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.api;

import java.util.List;

import net.blog.dev.gestion.stocks.middle.beans.StockListBean;
import net.blog.dev.gestion.stocks.middle.beans.StockListCloseBean;

/**
 * @author Kiva
 * 
 */
public interface IStocksListMService {

	List<StockListBean> getStocksListRunning();

	List<StockListCloseBean> getStocksListClose();

}
