/**
 * 
 */
package net.blog.dev.gestion.stocks.back.services.api;

import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 * @author Kiva doc:
 *         http://developer.yahoo.com/yql/console/?q=show%20tables&env=
 *         store://datatables.org/alltableswithkeys#h=show%20tables%3B <br>
 *         Sante des tables : http://www.datatables.org/healthchecker/ <br>
 *         Exemple de
 *         requete:http://query.yahooapis.com/v1/public/yql?q=select%20
 *         *%20from%20
 *         yahoo.finance.quotes%20where%20symbol%20in%20(%22ACA.PA%22)
 *         &format=json
 *         &env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=cbfunc
 */

@Path("/yql")
public interface IYahooFinanceBService {

	@Path("/")
	public IQuoteResource getQuote(@QueryParam("q") String query,
			@QueryParam("format") String format, @QueryParam("env") String env,
			@QueryParam("callback") String callback);
}
