/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.api;

import net.blog.dev.gestion.stocks.middle.beans.DetailStockBean;
import net.blog.dev.gestion.stocks.middle.beans.StockListBean;

/**
 * @author Kiva
 * 
 */
public interface IDetailStockMService {

	DetailStockBean getDetailStockBean(StockListBean stockListBean);

	/**
	 * Met a jour l'atr d'une position
	 * 
	 * @param stockListBean
	 * @param atr
	 */
	void updateAtr(StockListBean stockListBean, Float atr);

	/**
	 * Met a jour la quantite d'une position<br>
	 * Entraine la mise a jour du prix unitaire<br>
	 * Met a jour eglament le bean envoye
	 * 
	 * @param stockListBean
	 * @param quantity
	 */
	void updateQuantity(StockListBean stockListBean, Integer quantity);
}
