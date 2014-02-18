/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.beans;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author Kiva
 * 
 */
public class AddMovementBean {

	@NotNull(message = "error.null")
	@Pattern(regexp = "^[0-9,.]+$", message = "error.float.wrongFormat")
	private String total;

	@NotNull(message = "error.null")
	@Pattern(regexp = "^[0-9]{2}/[0-9]{2}/[0-9]{4}$", message = "error.date.wrongFormat")
	private String date;

	private boolean provision;

	private String comment;

	@NotNull(message = "error.null")
	private String account;

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
	 * @return the provision
	 */
	public boolean isProvision() {
		return provision;
	}

	/**
	 * @param provision
	 *            the provision to set
	 */
	public void setProvision(boolean provision) {
		this.provision = provision;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the total
	 */
	public String getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(String total) {
		this.total = total;
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
}