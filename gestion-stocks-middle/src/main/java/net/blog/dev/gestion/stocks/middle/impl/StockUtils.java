/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.impl;

import net.blog.dev.gestion.stocks.dto.DirectionEnum;
import net.blog.dev.gestion.stocks.dto.DtoStock;

import org.joda.time.DateTime;

/**
 * @author Kiva
 * 
 */
public class StockUtils {

	/**
	 * @param stock
	 * @param year
	 */
	public static Float getBenefits(DtoStock stock) {
		if (stock == null || stock.getSellOrder() == null
				|| stock.getBuyOrdre() == null)
			return 0.0f;
		if (stock.getDirection().equals(DirectionEnum.BUY))
			return stock.getSellOrder().getTotalPrice()
					- stock.getBuyOrdre().getTotalPrice();
		else
			return stock.getBuyOrdre().getTotalPrice()
					- stock.getSellOrder().getTotalPrice();
	}

	public static Float getBenefitsLessTaxes(DtoStock stock) {
		if (stock == null || stock.getSellOrder() == null
				|| stock.getBuyOrdre() == null)
			return 0.0f;
		Float taxes = stock.getSellOrder().getTaxes()
				+ stock.getBuyOrdre().getTaxes();
		if (stock.getDirection().equals(DirectionEnum.BUY))
			return stock.getSellOrder().getTotalPrice()
					- stock.getBuyOrdre().getTotalPrice() - taxes;
		else
			return stock.getBuyOrdre().getTotalPrice()
					- stock.getSellOrder().getTotalPrice() - taxes;
	}

	public static Integer getYearSold(DtoStock stock) {
		if (stock == null || stock.getSellOrder() == null)
			return 0;
		return new DateTime(stock.getSellOrder().getDate()).getYear();
	}

	public static Integer getYearBuy(DtoStock stock) {
		if (stock == null || stock.getBuyOrdre() == null)
			return 0;
		return new DateTime(stock.getBuyOrdre().getDate()).getYear();
	}

}
