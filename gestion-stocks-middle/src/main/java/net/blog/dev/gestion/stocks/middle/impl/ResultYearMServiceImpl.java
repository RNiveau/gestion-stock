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

import net.blog.dev.gestion.stocks.back.daos.IMovementDao;
import net.blog.dev.gestion.stocks.back.daos.IStockDao;
import net.blog.dev.gestion.stocks.dto.DtoDividend;
import net.blog.dev.gestion.stocks.dto.DtoMovement;
import net.blog.dev.gestion.stocks.dto.DtoStock;
import net.blog.dev.gestion.stocks.middle.api.IResultYearMService;
import net.blog.dev.gestion.stocks.middle.beans.ResultYearBean;

import org.joda.time.DateTime;

/**
 * @author Kiva
 * 
 */
@ApplicationScoped
public class ResultYearMServiceImpl extends AbstractMService implements
		IResultYearMService {

	@Inject
	private IStockDao stockDao;

	@Inject
	private IMovementDao movementDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.blog.dev.gestion.stocks.middle.api.IResultYearMService#getResultList
	 * ()
	 */
	@Override
	public List<ResultYearBean> getResultList() {
		final List<DtoStock> listStocks = stockDao.getListStocks();
		sortStockList(listStocks);
		List<ResultYearBean> resultList = new ArrayList<>();

		// liste de position en cours a travers les annees
		List<DtoStock> listRunning = new ArrayList<>();
		// variable pour savoir qu'elle annee en parcours
		int yearRunning = 0;
		if (listStocks != null) {
			for (DtoStock stock : listStocks) {
				if (stock != null && stock.getBuyOrdre() != null) {
					ResultYearBean year = getResultYearBean(resultList, stock
							.getBuyOrdre().getDate());
					// on passe sur une autre annee, on parcours la liste des
					// positions passees d'une annee sur l'autre
					if (yearRunning != 0 && yearRunning != year.getYear()) {
						// gestion des annees sans position
						while (yearRunning + 1 != year.getYear()) {
							yearRunning += 1;
							ResultYearBean yearWithoutPosition = getResultYearBean(
									resultList, yearRunning);
							listRunning = manageRunningPosition(listRunning,
									yearWithoutPosition);
						}
						listRunning = manageRunningPosition(listRunning, year);
						yearRunning = year.getYear();
					} else if (yearRunning == 0)
						yearRunning = year.getYear();
					year.addOrder();
					year.addInvested(stock.getBuyOrdre().getTotalPrice());
					year.addTaxes(stock.getBuyOrdre().getTaxes());
					// si vendu
					if (stock.getSellOrder() != null) {
						if (isSameYear(stock.getBuyOrdre().getDate(), stock
								.getSellOrder().getDate())) {
							year.addOrderClosed();
							manageSoldStock(stock, year);
						} else {
							year.addRunning(stock.getBuyOrdre().getTotalPrice());
							listRunning.add(stock);
						}
					} else {
						year.addRunning(stock.getBuyOrdre().getTotalPrice());
						listRunning.add(stock);
					}
					manageDividends(stock, year);
				}
			}
		}
		sortResultList(resultList);
		return resultList;
	}

	/**
	 * @param resultList
	 */
	private void sortResultList(List<ResultYearBean> resultList) {
		Collections.sort(resultList, new Comparator<ResultYearBean>() {

			@Override
			public int compare(ResultYearBean o1, ResultYearBean o2) {
				if (o1 == null || o2 == null || o1.getYear() == null
						|| o2.getYear() == null)
					return 0;
				if (o1.getYear() < o2.getYear())
					return -1;
				else if (o1.getYear() == o2.getYear())
					return 0;
				return 1;
			}
		});
	}

	/**
	 * @param listRunning
	 * @param yearRunning
	 * @param yearWithoutPosition
	 * @return
	 */
	private List<DtoStock> manageRunningPosition(List<DtoStock> listRunning,
			ResultYearBean yearWithoutPosition) {
		List<DtoStock> tmpListRunning = new ArrayList<>();
		for (DtoStock stockRunning : listRunning) {
			if (stockRunning.getSellOrder() != null
					&& isSameYear(yearWithoutPosition.getYear(), stockRunning
							.getSellOrder().getDate())) {
				yearWithoutPosition.addOrderPastYear();
				manageSoldStock(stockRunning, yearWithoutPosition);
			} else {
				tmpListRunning.add(stockRunning);
			}
			// On ajout les sommes investi dans les investissements des annees
			// passees
			yearWithoutPosition.addRunningPastYear(stockRunning.getBuyOrdre()
					.getTotalPrice());
			manageDividends(stockRunning, yearWithoutPosition);
		}
		return tmpListRunning;
	}

	/**
	 * @param stock
	 * @param year
	 */
	private void manageDividends(DtoStock stock, ResultYearBean year) {
		// parcours des dividendes
		if (stock.getDividends() != null) {
			for (DtoDividend dividend : stock.getDividends()) {
				if (dividend != null
						&& isSameYear(year.getYear(), dividend.getDate())) {
					year.addDividens(dividend.getTotalPrice());
				}
			}
		}
	}

	/**
	 * @param stock
	 * @param year
	 */
	private void manageSoldStock(DtoStock stock, ResultYearBean year) {
		year.addBenefit(StockUtils.getBenefits(stock));
		year.addTaxes(stock.getSellOrder().getTaxes());
		year.addSold(stock.getSellOrder().getTotalPrice());
	}

	private boolean isSameYear(Integer year, Date date) {
		if (year == null || date == null)
			return false;
		DateTime dateTime = new DateTime(date);
		return year == dateTime.getYear();
	}

	private boolean isSameYear(Date date1, Date date2) {
		if (date1 == null || date2 == null)
			return false;
		DateTime dateTime1 = new DateTime(date1);
		DateTime dateTime2 = new DateTime(date2);
		return dateTime1.getYear() == dateTime2.getYear();
	}

	/**
	 * Retourne l'element existant de la liste ou en creer un et le retourne si
	 * il n'existe pas
	 * 
	 * @param resultList
	 * @return
	 */
	private ResultYearBean getResultYearBean(List<ResultYearBean> resultList,
			Date year) {
		if (year != null) {
			DateTime date = new DateTime(year);
			return getResultYearBean(resultList, date.getYear());
		}
		return new ResultYearBean();
	}

	private ResultYearBean getResultYearBean(List<ResultYearBean> resultList,
			Integer year) {
		ResultYearBean resultYearBean = new ResultYearBean();
		if (year != null && resultList != null) {
			for (ResultYearBean result : resultList) {
				if (result.getYear().equals(year))
					return result;
			}
			// On creer une nouvelle annee, calcul du budget
			resultYearBean.setYear(year);
			prepareBudget(resultYearBean);
			resultList.add(resultYearBean);
		}
		return resultYearBean;

	}

	private void prepareBudget(ResultYearBean resultYearBean) {
		List<DtoMovement> listMovement = movementDao.getListMovements();
		Float budget = 0f;
		if (listMovement != null) {
			sortMovementList(listMovement);
			for (DtoMovement movement : listMovement) {
				if (new DateTime(movement.getDate()).getYear() <= resultYearBean
						.getYear()) {
					budget += movement.isProvision() ? movement.getTotal()
							: -movement.getTotal();
				}
			}
		}
		resultYearBean.setBudget(budget);
	}
}
