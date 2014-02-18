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
public class CompareStockBean {

	private Integer duration;

	private List<String> listCode;

	public void addValue(String value) {
		if (listCode == null)
			listCode = new ArrayList<>();
		listCode.add(value);
	}

	/**
	 * @return the duration
	 */
	public Integer getDuration() {
		return duration;
	}

	/**
	 * @param duration
	 *            the duration to set
	 */
	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	/**
	 * @return the listCode
	 */
	public List<String> getListCode() {
		return listCode;
	}

	/**
	 * @param listCode
	 *            the listCode to set
	 */
	public void setListCode(List<String> listCode) {
		this.listCode = listCode;
	}

}
