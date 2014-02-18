package net.blog.dev.gestion.stocks.back.services;

import java.io.IOException;

import net.blog.dev.gestion.stocks.back.json.Quote;
import net.blog.dev.gestion.stocks.back.services.api.IQuoteResource;
import net.blog.dev.gestion.stocks.back.services.api.IYahooFinanceBService;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class TestYahooService {

	public void test() {
		final IYahooFinanceBService factory = JAXRSClientFactory.create(
				"http://query.yahooapis.com/v1/public/",
				IYahooFinanceBService.class);
		final IQuoteResource quote = factory
				.getQuote(
						"select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22KN.PA%22)",
						"json",
						"store%3A%2F%2Fdatatables.org%2Falltableswithkeys", "");

		// http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22ACA.PA%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=cbfunc
		String quote2 = quote.getQuote();
		quote2 = quote2.replaceAll("^.*\\\"quote\\\":", "");
		quote2 = quote2.replaceAll("}}$", "");
		System.out.println(quote2);
		ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
		try {
			Quote quote3 = mapper.readValue(quote2, Quote.class);
			quote3 = quote3;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
