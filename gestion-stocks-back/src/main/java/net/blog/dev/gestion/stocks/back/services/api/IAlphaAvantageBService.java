package net.blog.dev.gestion.stocks.back.services.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("/")
@Produces("application/json")
public interface IAlphaAvantageBService {

    @Path("/query")
    public IQuoteResource getQuote(@QueryParam("function") String function,
                                   @QueryParam("symbol") String symbol,
                                   @QueryParam("apikey") String apikey);

}
