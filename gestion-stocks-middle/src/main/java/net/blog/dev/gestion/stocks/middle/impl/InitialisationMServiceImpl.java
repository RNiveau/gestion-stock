/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.impl;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import net.blog.dev.gestion.stocks.back.InitApp;
import net.blog.dev.gestion.stocks.back.daos.IMovementDao;
import net.blog.dev.gestion.stocks.back.daos.IStockDao;
import net.blog.dev.gestion.stocks.dto.AccountEnum;
import net.blog.dev.gestion.stocks.dto.DtoMovement;
import net.blog.dev.gestion.stocks.dto.DtoStock;
import net.blog.dev.gestion.stocks.middle.api.IInitialisationMService;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

/**
 * @author Kiva
 * 
 */
@ApplicationScoped
public class InitialisationMServiceImpl extends AbstractMService implements
		IInitialisationMService {

	@Inject
	private IStockDao stockDao;

	@Inject
	private IMovementDao movementDao;

	static String MOVEMENT_SOLD = "Cumul des ventes de fin d'annee";

	static String MOVEMENT_TAXES = "Cumul des taxes de fin d'annee";

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.blog.dev.gestion.stocks.middle.api.IInitialisation#init()
	 */
	@Override
	public void init(@Observes @InitApp String event) {
		automaticMovements();
	}

	/**
	 * Gere les mouvements automatiques de fin d'annee (cumul de vendu)
	 */
	public void automaticMovements() {
		final List<DtoStock> listStocks = stockDao.getListStocks();
		final List<DtoMovement> listMovements = movementDao.getListMovements();
		sortStockList(listStocks);

		int year = 0;
		int currentYear = new DateTime().getYear();
		if (listStocks.size() > 0) {
			// initialisation de l'annee
			DateTime date = new DateTime(listStocks.get(0).getBuyOrdre()
					.getDate());
			year = date.getYear();
		}
		// on parcours toutes les annees sauf cette annee
		while (year < currentYear) {
			float totalCourant = 0.0f;
			float totalPea = 0.0f;
			float totalTaxesPea = 0.0f;
			float totalTaxesCourant = 0.0f;
			// si l'un ou l'autre n'existe pas, on reparcours
			// le mouvement est tjs enregistrer annee + 1
			if (!soldMovementExist(year + 1, listMovements, AccountEnum.PEA)
					|| !soldMovementExist(year + 1, listMovements,
							AccountEnum.SECURITIES)) {
				for (DtoStock stock : listStocks) {
					final Integer yearBuy = StockUtils.getYearBuy(stock);
					// gestion taxes d'achat
					if (year == yearBuy) {
						if (AccountEnum.PEA.equals(stock.getAccount()))
							totalTaxesPea += stock.getBuyOrdre().getTaxes();
						else
							totalTaxesCourant += stock.getBuyOrdre().getTaxes();
					}
					final Integer yearSold = StockUtils.getYearSold(stock);
					// vente + taxes de vente
					if (year == yearSold) {
						if (AccountEnum.PEA.equals(stock.getAccount())) {
							totalPea += StockUtils.getBenefits(stock);
							totalTaxesPea += stock.getSellOrder().getTaxes();
						} else {
							totalCourant += StockUtils.getBenefits(stock);
							totalTaxesCourant += stock.getSellOrder()
									.getTaxes();
						}
					}
				}
				// on incremente maintenant car le mouvement est sur le 1
				// janvier de l'annee d'apres
				year++;
				if (!soldMovementExist(year, listMovements, AccountEnum.PEA,
						MOVEMENT_SOLD) && totalPea > 0.0f) {
					generateMovementSold(year, totalPea, AccountEnum.PEA);
				}
				if (!soldMovementExist(year, listMovements, AccountEnum.PEA,
						MOVEMENT_TAXES) && totalTaxesPea > 0.0f) {
					generateMovementTaxes(year, totalTaxesPea, AccountEnum.PEA);
				}
				if (!soldMovementExist(year, listMovements,
						AccountEnum.SECURITIES, MOVEMENT_SOLD)
						&& totalCourant > 0.0f) {
					generateMovementSold(year, totalCourant,
							AccountEnum.SECURITIES);
				}
				if (!soldMovementExist(year, listMovements,
						AccountEnum.SECURITIES, MOVEMENT_TAXES)
						&& totalTaxesCourant > 0.0f) {
					generateMovementTaxes(year, totalTaxesCourant,
							AccountEnum.SECURITIES);
				}
			} else
				year++;
		}
	}

	/**
	 * @param year
	 * @param totalCourant
	 */
	private void generateMovementSold(int year, float totalCourant,
			AccountEnum account) {
		final DtoMovement dtoMovement = new DtoMovement();
		dtoMovement.setAutomatic(true);
		dtoMovement.setComment(MOVEMENT_SOLD);
		dtoMovement.setAccount(account);
		dtoMovement.setProvision(true);
		dtoMovement.setTotal(totalCourant);
		dtoMovement.setDate(new DateTime(year, 1, 1, 1, 1).toDate());
		movementDao.saveMovement(dtoMovement);
	}

	/**
	 * @param year
	 * @param totalCourant
	 */
	private void generateMovementTaxes(int year, float totalCourant,
			AccountEnum account) {
		final DtoMovement dtoMovement = new DtoMovement();
		dtoMovement.setAutomatic(true);
		dtoMovement.setComment(MOVEMENT_TAXES);
		dtoMovement.setAccount(account);
		dtoMovement.setProvision(false);
		dtoMovement.setTotal(totalCourant);
		dtoMovement.setDate(new DateTime(year, 1, 1, 1, 1).toDate());
		movementDao.saveMovement(dtoMovement);
	}

	private boolean soldMovementExist(int year,
			List<DtoMovement> listMovements, AccountEnum account) {
		return soldMovementExist(year, listMovements, account, MOVEMENT_SOLD);
	}

	private boolean soldMovementExist(int year,
			List<DtoMovement> listMovements, AccountEnum account, String comment) {
		for (DtoMovement movement : listMovements) {
			if (movement.getDate() != null) {
				final int movementYear = new DateTime(movement.getDate())
						.getYear();
				if (movementYear == year) {
					if (movement.isAutomatic()
							&& StringUtils.equals(comment,
									movement.getComment())
							&& account.equals(movement.getAccount()))
						return true;
				}
			}
		}
		return false;
	}

}
