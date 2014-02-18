/**
 * 
 */
package net.blog.dev.gestion.stocks.back.migration.versions;

import javax.enterprise.inject.Default;

import net.blog.dev.gestion.stocks.back.KContext;
import net.blog.dev.gestion.stocks.back.migration.IMigrationVersion;
import net.blog.dev.gestion.stocks.dto.DtoDividend;
import net.blog.dev.gestion.stocks.dto.DtoPortfolio;
import net.blog.dev.gestion.stocks.dto.DtoStock;

/**
 * @author Kiva
 * 
 */
@Default
public class MigrationVersion1_2 implements IMigrationVersion {

	@Override
	public String getVersion() {
		return "1.2.0";
	}

	/**
	 * Mise a jour des dividendes suite a l'ajout du prix total
	 */
	@Override
	public void migrate(KContext context) {
		final DtoPortfolio portfolio = context.getPortfolio();
		if (portfolio.getStocks() != null) {
			for (DtoStock stock : portfolio.getStocks()) {
				if (stock != null && stock.getDividends() != null) {
					for (DtoDividend dividend : stock.getDividends()) {
						dividend.setTotalPrice(dividend.getUnitPrice()
								* stock.getQuantity());
					}
				}
			}
			context.writeFile();
		}
	}

}
