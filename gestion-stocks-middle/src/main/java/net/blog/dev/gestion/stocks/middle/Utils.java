/**
 * 
 */
package net.blog.dev.gestion.stocks.middle;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

/**
 * @author Kiva
 * 
 */
public class Utils {

	static public Float getFloat(String fl) {
		try {
			if (fl == null)
				return null;
			return Float.valueOf(fl.replaceAll(",", "."));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	static public Integer getInteger(String in) {
		try {
			return Integer.valueOf(in);
		} catch (NullPointerException | NumberFormatException e) {
			return null;
		}
	}

	static public Date getDate(String date, String pattern) {
		try {
			return DateUtils.parseDate(date, pattern);
		} catch (ParseException e) {
			return null;
		}
	}

	static public String formatDate(Date date, String pattern) {
		final DateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(date);
	}

}
