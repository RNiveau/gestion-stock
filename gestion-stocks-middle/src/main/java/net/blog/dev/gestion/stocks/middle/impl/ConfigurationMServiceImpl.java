/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import net.blog.dev.gestion.stocks.back.KContext;
import net.blog.dev.gestion.stocks.dto.KConfiguration;
import net.blog.dev.gestion.stocks.middle.api.IConfigurationMSservice;
import net.blog.dev.gestion.stocks.middle.beans.ConfigurationBean;

/**
 * @author Kiva
 * 
 */
@ApplicationScoped
public class ConfigurationMServiceImpl implements IConfigurationMSservice {

	@Inject
	private KContext context;

	@Override
	public ConfigurationBean getConfiguration() {
		final KConfiguration configuration = context.getConfiguration();
		final ConfigurationBean configurationBean = new ConfigurationBean();
		configurationBean.setDirectory(configuration.getDirectory());
		configurationBean.setSrdLoan(configuration.getSrdLoan());
		return configurationBean;
	}

	@Override
	public void setConfiguration(ConfigurationBean configurationBean) {
		final KConfiguration configuration = context.getConfiguration();
		if (configurationBean.getDirectory() != null)
			configuration.setDirectory(configurationBean.getDirectory());
		if (configurationBean.getSrdLoan() != null)
			configuration.setSrdLoan(configurationBean.getSrdLoan());
		context.saveConfiguration();
	}
}
