/**
 * 
 */
package net.blog.dev.gestion.stocks.middle;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author Kiva
 * 
 */
public class CalculUtils {

    static final Logger logger = LoggerFactory.getLogger(CalculUtils.class);

	/**
	 * Ajoute un pourcentage a la value
	 * 
	 * @param value
	 * @param percentage
	 * @return
	 */
	static public Float addPercentage(Float value, Float percentage) {
        logger.debug("addPercentage {}, {}", value, percentage);
		if (value == null || percentage == null)
			return 0f;
		if (percentage >= 0)
			return value * (1 + (percentage / 100));
		return value * (1 + (percentage / 100));
	}

	/**
	 * Donne la difference en pourcentage entre f1 et f2<br>
	 * Renvoi toujours un taux positif
	 * 
	 * @param f1
	 * @param f2
	 * @return
	 */
	static public Float getPercentageBetweenTwoValues(Float f1, Float f2) {
        logger.debug("getPercentageBetweenTwoValues {}, {}", f1, f2);
		if (f1 == null || f2 == null)
			return 0f;
		Float f3 = f2 / f1;
		// taux positif
		if (f3 > 1.0f)
			return (f3 - 1f) * 100;
		return 100 - (f3 * 100);
	}

	/**
	 * f1 est x % de f2
	 * 
	 * @param f1
	 * @param f2
	 * @return
	 */
	static public Float getPercentageIntoValues(Float f1, Float f2) {
        logger.debug("getPercentageIntoValues {}, {}", f1, f2);
		if (f1 == null || f2 == null || f2 == 0.0f)
			return 0f;
		return (f1 / f2) * 100f;
	}

	/**
	 * Retourne le nombre de jour entre date et aujourd'hui
	 * 
	 * @param date
	 * @return
	 */
	static public Integer getDayWithNow(Date date) {
        logger.debug("getDayWithNow {}", date);
		return getDaysBetweenDate(date, Instant.now().toDate());
	}

	/**
	 * Retourne le nombre de jour entre date1 et date2
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	static public Integer getDaysBetweenDate(Date date1, Date date2) {
        logger.debug("getDaysBetweenDate {}, {}", date1, date2);
		if (date1 == null || date2 == null)
			return 0;
		Interval interval = new Interval(date1.getTime(), date2.getTime());
		Duration d = new Duration(interval);
		return Integer.parseInt(((Long) d.getStandardDays()).toString());

	}
}
