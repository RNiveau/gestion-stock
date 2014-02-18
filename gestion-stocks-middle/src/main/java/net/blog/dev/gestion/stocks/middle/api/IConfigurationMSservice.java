/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.api;

import net.blog.dev.gestion.stocks.middle.beans.ConfigurationBean;

/**
 * @author Kiva
 * 
 */
public interface IConfigurationMSservice {

	ConfigurationBean getConfiguration();

	void setConfiguration(ConfigurationBean configurationBean);

}
