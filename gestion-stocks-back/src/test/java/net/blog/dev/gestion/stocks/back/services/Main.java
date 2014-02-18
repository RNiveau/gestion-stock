package net.blog.dev.gestion.stocks.back.services;

import java.util.List;

import net.blog.dev.gestion.stocks.back.json.HistoricQuote;
import net.blog.dev.gestion.stocks.back.json.Quote;
import net.blog.dev.gestion.stocks.back.services.impl.QuoteBServiceImpl;

import org.joda.time.DateTime;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final TestYahooService testService = new TestYahooService();
		testService.test();

		final QuoteBServiceImpl quoteBServiceImpl = new QuoteBServiceImpl();
		final Quote quote = quoteBServiceImpl.getQuote("ACA");
		System.out.println(quote.getSymbol() + quote.getBid());

		final List<Quote> quotes = quoteBServiceImpl.getQuotes("ACA", "KN");
		for (Quote q : quotes)
			System.out.println(q.getSymbol() + q.getBid());

		final List<HistoricQuote> historicQuote = quoteBServiceImpl
				.getHistoricQuote("ACA", new DateTime(2012, 7, 31, 1, 1)
						.toDate(), new DateTime(2013, 7, 1, 1, 1).toDate());
		for (HistoricQuote h : historicQuote)
			System.out.println(h.getClose());
	}
}
