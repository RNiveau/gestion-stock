/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.beans;

/**
 * @author Kiva
 * 
 */
public class ConfigurationBean {

	private String directory;

	private Float srdLoan;

    private String idDropbox;

	/**
	 * @return the directory
	 */
	public String getDirectory() {
		return directory;
	}

	/**
	 * @param directory
	 *            the directory to set
	 */
	public void setDirectory(String directory) {
		this.directory = directory;
	}

	/**
	 * @return the srdLoan
	 */
	public Float getSrdLoan() {
		return srdLoan;
	}

	/**
	 * @param srdLoan
	 *            the srdLoan to set
	 */
	public void setSrdLoan(Float srdLoan) {
		this.srdLoan = srdLoan;
	}

    public String getIdDropbox() {
        return idDropbox;
    }

    public void setIdDropbox(String idDropbox) {
        this.idDropbox = idDropbox;
    }
}
