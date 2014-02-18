/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.beans;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import net.blog.dev.gestion.stocks.back.StockId;

/**
 * @author Kiva
 * 
 */
public class AddDividendBean {

	private StockId id;

	@NotNull(message = "error.null")
	@Pattern(regexp = "^[0-9,.]+$", message = "error.float.wrongFormat")
	private String price;

	@NotNull(message = "error.null")
	@Pattern(regexp = "^[0-9]{2}/[0-9]{2}/[0-9]{4}$", message = "error.date.wrongFormat")
	private String date;

	/**
	 * @return the id
	 */
	public StockId getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(StockId id) {
		this.id = id;
	}

	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}
}
