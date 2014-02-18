/**
 * 
 */
package net.blog.dev.gestion.stocks.back.services.api;

import java.util.Date;
import java.util.List;

import net.blog.dev.gestion.stocks.back.json.HistoricQuote;
import net.blog.dev.gestion.stocks.back.json.Quote;

/**
 * @author Kiva
 * 
 */
public interface IQuoteBService {

	public Quote getQuote(String code);

	public List<Quote> getQuotes(String... codes);

	public List<HistoricQuote> getHistoricQuote(String code, Date start,
			Date end);

}