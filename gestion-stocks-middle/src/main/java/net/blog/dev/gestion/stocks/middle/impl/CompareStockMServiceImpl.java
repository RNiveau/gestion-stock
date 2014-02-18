/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import net.blog.dev.gestion.stocks.back.json.HistoricQuote;
import net.blog.dev.gestion.stocks.back.services.api.IQuoteBService;
import net.blog.dev.gestion.stocks.middle.CalculUtils;
import net.blog.dev.gestion.stocks.middle.api.ICompareStockMService;
import net.blog.dev.gestion.stocks.middle.beans.CompareStockBean;
import net.blog.dev.gestion.stocks.middle.beans.CompareStockResultBean;
import net.blog.dev.gestion.stocks.middle.beans.CompareStockValueBean;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.joda.time.Instant;

/**
 * @author Kiva
 * 
 */
@ApplicationScoped
public class CompareStockMServiceImpl extends AbstractMService implements
		ICompareStockMService {

	@Inject
	private IQuoteBService quoteBService;

	@Override
	public List<CompareStockResultBean> getListCompareStock(
			CompareStockBean compareStockBean) {
		List<CompareStockResultBean> list = new ArrayList<>();
		Integer duration = 1;
		if (compareStockBean != null && compareStockBean.getDuration() != null)
			duration = compareStockBean.getDuration();
		Date start = Instant.now().toDate();
		Date end = new DateTime(start.getTime()).minusMonths(duration).toDate();

		if (compareStockBean != null
				&& CollectionUtils.isNotEmpty(compareStockBean.getListCode())) {
			for (String code : compareStockBean.getListCode()) {
				final List<HistoricQuote> historicQuote = quoteBService
						.getHistoricQuote(code, end, start);
				analyseQuotes(list, code, historicQuote);
			}
		}
		return list;
	}

	/**
	 * @param list
	 * @param code
	 * @param historicQuote
	 */
	private void analyseQuotes(List<CompareStockResultBean> list, String code,
			final List<HistoricQuote> historicQuote) {
		if (CollectionUtils.isNotEmpty(historicQuote)) {
			Collections.sort(historicQuote, new Comparator<HistoricQuote>() {

				@Override
				public int compare(HistoricQuote o1, HistoricQuote o2) {
					if (o1 == null || o2 == null || o1.getDate() == null
							|| o2.getDate() == null)
						return 0;
					return o1.getDate().compareTo(o2.getDate());
				}

			});
			CompareStockResultBean compareStockResultBean = new CompareStockResultBean();
			compareStockResultBean.setName(code);
			Float initialPrice = 0f;
			Float rate = 0f;
			for (HistoricQuote quote : historicQuote) {
				if (initialPrice == 0f)
					initialPrice = quote.getClose();
				else {
					final Float percentage = CalculUtils
							.getPercentageBetweenTwoValues(initialPrice,
									quote.getClose());
					rate += initialPrice > quote.getClose() ? -percentage
							: percentage;
					initialPrice = quote.getClose();
				}
				final CompareStockValueBean compareStockValueBean = new CompareStockValueBean();
				compareStockValueBean.setDate(quote.getDate());
				compareStockValueBean.setValue(rate);
				compareStockResultBean.addValue(compareStockValueBean);
			}
			// Calcul lower et higher
			Float lower = 0f;
			Float higher = 0f;
			for (CompareStockValueBean compareStockValueBean : compareStockResultBean
					.getListValue()) {
				if (compareStockValueBean.getValue() > higher)
					higher = compareStockValueBean.getValue();
				if (compareStockValueBean.getValue() < lower)
					lower = compareStockValueBean.getValue();
			}
			compareStockResultBean.setHigher(higher);
			compareStockResultBean.setLower(lower);
			list.add(compareStockResultBean);
		}
	}
}
