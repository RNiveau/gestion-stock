package net.blog.dev.gestion.stocks.back.services.api;

import javax.ws.rs.GET;

public interface IQuoteResource {

	@GET
	public String getQuote();

}