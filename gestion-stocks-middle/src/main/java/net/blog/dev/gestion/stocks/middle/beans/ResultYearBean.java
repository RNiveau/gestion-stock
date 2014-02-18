/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.beans;

import net.blog.dev.gestion.stocks.middle.CalculUtils;

/**
 * @author Kiva
 * 
 */
public class ResultYearBean {

	private Integer year;

	private Integer nbrOrder;

	/**
	 * Nombre de position cloture dans l'annee
	 */
	private Integer nbrOrderClosed;

	/**
	 * Nombre d'ordre cloture des annees passees
	 */
	private Integer nbrOrderPastYear;

	/**
	 * Total engagement dans l'annee
	 */
	private Float totalInvested;

	/**
	 * Total vendu dans l'annee meme si la position est plus ancienne
	 */
	private Float totalSold;

	/**
	 * Total qui reste investi apres l'annee
	 */
	private Float totalRunning;

	/**
	 * Total investi qui provient des annees precedantes (vendu ou pas)
	 */
	private Float totalRunningPastYear;

	private Float taxes;

	private Float benefit;

	private Float totalDividends;

	private Float budget;

	public ResultYearBean() {
		year = 0;
		nbrOrder = 0;
		nbrOrderClosed = 0;
		nbrOrderPastYear = 0;
		totalInvested = 0f;
		totalSold = 0f;
		totalRunning = 0f;
		taxes = 0f;
		benefit = 0f;
		totalDividends = 0f;
		totalRunningPastYear = 0f;
	}

	public void addOrder() {
		nbrOrder += 1;
	}

	public void addOrderClosed() {
		nbrOrderClosed += 1;
	}

	public void addOrderPastYear() {
		nbrOrderPastYear += 1;
	}

	public void addInvested(Float value) {
		totalInvested += value;
	}

	public void addSold(Float value) {
		totalSold += value;
	}

	public void addRunning(Float value) {
		totalRunning += value;
	}

	public void addRunningPastYear(Float value) {
		totalRunningPastYear += value;
	}

	public void addTaxes(Float value) {
		taxes += value;
	}

	public void addBenefit(Float value) {
		benefit += value;
	}

	public void addDividens(Float value) {
		totalDividends += value;
	}

	/**
	 * @return the year
	 */
	public Integer getYear() {
		return year;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear(Integer year) {
		this.year = year;
	}

	/**
	 * @return the nbrOrder
	 */
	public Integer getNbrOrder() {
		return nbrOrder;
	}

	/**
	 * @return the nbrOrderClosed
	 */
	public Integer getNbrOrderClosed() {
		return nbrOrderClosed;
	}

	/**
	 * @return the nbrOrderPastYear
	 */
	public Integer getNbrOrderPastYear() {
		return nbrOrderPastYear;
	}

	/**
	 * @return the totalInvested
	 */
	public Float getTotalInvested() {
		return totalInvested;
	}

	/**
	 * @return the totalSold
	 */
	public Float getTotalSold() {
		return totalSold;
	}

	/**
	 * @return the totalRunning
	 */
	public Float getTotalRunning() {
		return totalRunning;
	}

	/**
	 * @return the taxes
	 */
	public Float getTaxes() {
		return taxes;
	}

	/**
	 * @return the benefit
	 */
	public Float getBenefit() {
		return benefit;
	}

	/**
	 * @return the benefitWithTaxes
	 */
	public Float getBenefitWithTaxes() {
		return benefit - taxes;
	}

	/**
	 * @return the benefitPercentage
	 */
	public Float getBenefitPercentage() {
		return CalculUtils.getPercentageIntoValues(getBenefit(),
				getTotalInvested() + getTotalRunningPastYear());
	}

	/**
	 * @return the benefitWithTaxesPercentage
	 */
	public Float getBenefitWithTaxesPercentage() {
		return CalculUtils.getPercentageIntoValues(getBenefitWithTaxes(),
				getTotalInvested() + getTotalRunningPastYear());
	}

	/**
	 * @return the totalDividends
	 */
	public Float getTotalDividends() {
		return totalDividends;
	}

	/**
	 * @return the totalRunningPastYear
	 */
	public Float getTotalRunningPastYear() {
		return totalRunningPastYear;
	}

	/**
	 * @return the budget
	 */
	public Float getBudget() {
		return budget;
	}

	/**
	 * @param budget
	 *            the budget to set
	 */
	public void setBudget(Float budget) {
		this.budget = budget;
	}

	public Float getBenefitOnBudget() {
		return budget == null ? 0f : CalculUtils.getPercentageIntoValues(
				getBenefit(), budget);
	}

	public Float getBenefitOnBudgetWithTaxes() {
		return budget == null ? 0f : CalculUtils.getPercentageIntoValues(
				getBenefitWithTaxes(), budget);
	}
}
