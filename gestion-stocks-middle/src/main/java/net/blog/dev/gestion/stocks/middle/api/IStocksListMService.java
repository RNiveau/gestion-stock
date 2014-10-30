/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.api;

import net.blog.dev.gestion.stocks.middle.beans.StockListBean;
import net.blog.dev.gestion.stocks.middle.beans.StockListCloseBean;

import java.util.List;

/**
 * @author Kiva
 * 
 */
public interface IStocksListMService {

	List<StockListBean> getStocksListRunning();

    List<StockListBean> getStocksListRunning(boolean group);

	List<StockListCloseBean> getStocksListClose();

    List<StockListCloseBean> getStocksListClose(boolean group);

    Float getActualPrice(String code);
}
