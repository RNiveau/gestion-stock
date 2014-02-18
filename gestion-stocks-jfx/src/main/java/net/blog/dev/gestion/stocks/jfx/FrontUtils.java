/**
 * 
 */
package net.blog.dev.gestion.stocks.jfx;

import java.text.DecimalFormat;

/**
 * @author Kiva
 * 
 */
public class FrontUtils {

	/**
	 * Formatage des prix et pourcentage
	 * 
	 * @param price
	 * @return
	 */
	static public String formatPricePercentage(Float price) {
		final DecimalFormat format = new DecimalFormat("#,###.###");
		return format.format(price);
	}

}
