/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.beans;

import java.util.Date;

/**
 * @author Kiva
 * 
 */
public class CompareStockValueBean {

	private Float value;

	private Date date;

	/**
	 * @return the value
	 */
	public Float getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(Float value) {
		this.value = value;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

}
