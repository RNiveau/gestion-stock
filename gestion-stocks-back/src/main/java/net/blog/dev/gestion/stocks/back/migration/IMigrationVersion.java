/**
 * 
 */
package net.blog.dev.gestion.stocks.back.migration;

import net.blog.dev.gestion.stocks.back.KContext;

/**
 * @author Kiva
 * 
 */
public interface IMigrationVersion {

	String getVersion();

	void migrate(KContext context);

}
