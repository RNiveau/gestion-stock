/**
 * 
 */
package net.blog.dev.gestion.stocks.jaxb;

import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author Kiva
 * 
 */
public class DateXmlAdapter extends XmlAdapter<String, Date> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date unmarshal(String value) {
		if (value == null) {
			return null;
		}
		return DatatypeConverter.parseDate(value).getTime();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String marshal(Date value) {
		if (value == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(value);
		return DatatypeConverter.printDate(c);
	}
}
