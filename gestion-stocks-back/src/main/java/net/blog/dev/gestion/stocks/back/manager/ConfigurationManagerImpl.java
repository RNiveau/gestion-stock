/**
 * 
 */
package net.blog.dev.gestion.stocks.back.manager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import net.blog.dev.gestion.stocks.back.KContext;
import net.blog.dev.gestion.stocks.dto.DtoPortfolio;

/**
 * @author Kiva
 * 
 */
@ApplicationScoped
public class ConfigurationManagerImpl implements IConfigurationManager {

	@Inject
	private KContext context;

	@Override
	public DtoPortfolio getPortfolio() {
		return context.getPortfolio();
	}

	public void save() {
		context.writeFile();
	}

}
