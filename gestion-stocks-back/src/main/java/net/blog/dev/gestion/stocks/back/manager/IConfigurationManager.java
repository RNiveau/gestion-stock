/**
 * 
 */
package net.blog.dev.gestion.stocks.back.manager;

import net.blog.dev.gestion.stocks.dto.DtoPortfolio;

/**
 * @author Kiva
 * 
 */
public interface IConfigurationManager {

	DtoPortfolio getPortfolio();

	public void save();

}
