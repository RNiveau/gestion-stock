/**
 * 
 */
package net.blog.dev.gestion.stocks.jfx.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Kiva
 * 
 */
public class FrontUtils {

    private static final Logger logger = LoggerFactory.getLogger(FrontUtils.class);

    public static final String GREEN = "#09bf31";

    public static final String RED = "#f40c0c";

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

    static public String formatDate(Date date, String format) {
        if (date == null)
            return "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    static public Date formatStringToDate(String date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
        return null;
    }
}
