/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.api;

import net.blog.dev.gestion.stocks.middle.beans.ConfigurationBean;

/**
 * @author Kiva
 * 
 */
public interface IConfigurationMService {

	ConfigurationBean getConfiguration();

	void setConfiguration(ConfigurationBean configurationBean);

    String getDropboxUrl();

    void saveDropboxAccount(String code);

    void saveOnExit();

    void getFromDropbox();

    boolean newerFileExist();

}
