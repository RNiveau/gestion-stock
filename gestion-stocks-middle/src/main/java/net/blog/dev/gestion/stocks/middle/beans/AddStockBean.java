/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.beans;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Kiva
 * 
 */
public class AddStockBean {

	@NotNull(message = "error.null")
	@NotEmpty(message = "error.null")
	private String name;

	@NotNull(message = "error.null")
	@Pattern(regexp = "^[a-zA-Z]+$", message = "error.code.wrongFormat")
	private String code;

	@NotNull(message = "error.null")
	@Pattern(regexp = "^[0-9]+$", message = "error.integer.wrongFormat")
	private String quantity;

	@NotNull(message = "error.null")
	@Pattern(regexp = "^[0-9,.]+$", message = "error.float.wrongFormat")
	private String price;

	@NotNull(message = "error.null")
	@Pattern(regexp = "^[0-9,.]+$", message = "error.float.wrongFormat")
	private String taxes;

	@NotNull(message = "error.null")
	@Pattern(regexp = "^[0-9]{2}/[0-9]{2}/[0-9]{4}$", message = "error.date.wrongFormat")
	private String date;

	@NotNull(message = "error.null")
	private String typeOrder;

	@NotNull(message = "error.null")
	private String direction;

	@NotNull(message = "error.null")
	private String account;

	@NotNull(message = "error.null")
	private String strategy;

	@Pattern(regexp = "^[0-9.,]*$", message = "error.float.wrongFormat")
	private String atr;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the typeOrder
	 */
	public String getTypeOrder() {
		return typeOrder;
	}

	/**
	 * @param typeOrder
	 *            the typeOrder to set
	 */
	public void setTypeOrder(String typeOrder) {
		this.typeOrder = typeOrder;
	}

	/**
	 * @return the direction
	 */
	public String getDirection() {
		return direction;
	}

	/**
	 * @param direction
	 *            the direction to set
	 */
	public void setDirection(String direction) {
		this.direction = direction;
	}

	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * @return the strategy
	 */
	public String getStrategy() {
		return strategy;
	}

	/**
	 * @param strategy
	 *            the strategy to set
	 */
	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	/**
	 * @return the quantity
	 */
	public String getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(String quantity) {
		this.quantity = quantity;
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
	 * @return the taxes
	 */
	public String getTaxes() {
		return taxes;
	}

	/**
	 * @param taxes
	 *            the taxes to set
	 */
	public void setTaxes(String taxes) {
		this.taxes = taxes;
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

	/**
	 * @return the atr
	 */
	public String getAtr() {
		return atr;
	}

	/**
	 * @param atr
	 *            the atr to set
	 */
	public void setAtr(String atr) {
		this.atr = atr;
	}

}
