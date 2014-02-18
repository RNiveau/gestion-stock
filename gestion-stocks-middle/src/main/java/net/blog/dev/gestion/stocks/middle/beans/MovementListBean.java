/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.beans;

/**
 * @author Kiva
 * 
 */
public class MovementListBean {

	private String total;

	private boolean automatic;

	private String provision;

	private String comment;

	private String date;

	private String account;

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
	 * @return the automatic
	 */
	public boolean isAutomatic() {
		return automatic;
	}

	/**
	 * @param automatic
	 *            the automatic to set
	 */
	public void setAutomatic(boolean automatic) {
		this.automatic = automatic;
	}

	/**
	 * @return the provision
	 */
	public String getProvision() {
		return provision;
	}

	/**
	 * @param provision
	 *            the provision to set
	 */
	public void setProvision(String provision) {
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
