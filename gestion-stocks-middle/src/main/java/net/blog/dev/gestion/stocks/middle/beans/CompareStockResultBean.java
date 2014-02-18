/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kiva
 * 
 */
public class CompareStockResultBean {

	private String name;

	private Float lower;

	private Float higher;

	private List<CompareStockValueBean> listValue;

	public void addValue(CompareStockValueBean value) {
		if (listValue == null)
			listValue = new ArrayList<>();
		listValue.add(value);
	}

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
	 * @return the listValue
	 */
	public List<CompareStockValueBean> getListValue() {
		return listValue;
	}

	/**
	 * @param listValue
	 *            the listValue to set
	 */
	public void setListValue(List<CompareStockValueBean> listValue) {
		this.listValue = listValue;
	}

	/**
	 * @return the higher
	 */
	public Float getHigher() {
		return higher;
	}

	/**
	 * @param higher
	 *            the higher to set
	 */
	public void setHigher(Float higher) {
		this.higher = higher;
	}

	/**
	 * @return the lower
	 */
	public Float getLower() {
		return lower;
	}

	/**
	 * @param lower
	 *            the lower to set
	 */
	public void setLower(Float lower) {
		this.lower = lower;
	}

}
