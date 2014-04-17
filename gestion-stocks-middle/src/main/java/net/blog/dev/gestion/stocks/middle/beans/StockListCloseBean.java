/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.beans;

import java.util.Date;

/**
 * @author Kiva
 * 
 */
public class StockListCloseBean extends StockListBean {

	private Float sellPrice;

	private Float sellUnitPrice;

	private Date sellDate;

	private Float sellTaxes;

	private Float gain;

	private Float gainLessTaxes;

	private Float gainPercentage;

	private Float gainLessTaxesPercentage;

	/**
	 * @return the sellDate
	 */
	public Date getSellDate() {
		return sellDate;
	}

	/**
	 * @param sellDate
	 *            the sellDate to set
	 */
	public void setSellDate(Date sellDate) {
		this.sellDate = sellDate;
	}

	/**
	 * @return the gain
	 */
	public Float getGain() {
		return gain;
	}

	/**
	 * @param gain
	 *            the gain to set
	 */
	public void setGain(Float gain) {
		this.gain = gain;
	}

	/**
	 * @return the gainLessTaxes
	 */
	public Float getGainLessTaxes() {
		return gainLessTaxes;
	}

	/**
	 * @param gainLessTaxes
	 *            the gainLessTaxes to set
	 */
	public void setGainLessTaxes(Float gainLessTaxes) {
		this.gainLessTaxes = gainLessTaxes;
	}

	/**
	 * @return the gainPercentage
	 */
	public Float getGainPercentage() {
		return gainPercentage;
	}

	/**
	 * @param gainPercentage
	 *            the gainPercentage to set
	 */
	public void setGainPercentage(Float gainPercentage) {
		this.gainPercentage = gainPercentage;
	}

	/**
	 * @return the gainLesTaxesPercentage
	 */
	public Float getGainLessTaxesPercentage() {
		return gainLessTaxesPercentage;
	}

	/**
	 * @param gainLesTaxesPercentage
	 *            the gainLesTaxesPercentage to set
	 */
	public void setGainLessTaxesPercentage(Float gainLesTaxesPercentage) {
		this.gainLessTaxesPercentage = gainLesTaxesPercentage;
	}

	/**
	 * @return the sellTaxes
	 */
	public Float getSellTaxes() {
		return sellTaxes;
	}

	/**
	 * @param sellTaxes
	 *            the sellTaxes to set
	 */
	public void setSellTaxes(Float sellTaxes) {
		this.sellTaxes = sellTaxes;
	}

	/**
	 * @return the sellPrice
	 */
	public Float getSellPrice() {
		return sellPrice;
	}

	/**
	 * @param sellPrice
	 *            the sellPrice to set
	 */
	public void setSellPrice(Float sellPrice) {
		this.sellPrice = sellPrice;
	}

	/**
	 * @return the sellUnitPrice
	 */
	public Float getSellUnitPrice() {
		return sellUnitPrice;
	}

	/**
	 * @param sellUnitPrice
	 *            the sellUnitPrice to set
	 */
	public void setSellUnitPrice(Float sellUnitPrice) {
		this.sellUnitPrice = sellUnitPrice;
	}

}
