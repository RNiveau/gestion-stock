/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.beans;

/**
 * @author Kiva
 * 
 */
public class DividendBean {

	private String date;

	private Float price;

	private Float percentage;

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
	 * @return the price
	 */
	public Float getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(Float price) {
		this.price = price;
	}

	/**
	 * @return the percentage
	 */
	public Float getPercentage() {
		return percentage;
	}

	/**
	 * @param percentage
	 *            the percentage to set
	 */
	public void setPercentage(Float percentage) {
		this.percentage = percentage;
	}

}
