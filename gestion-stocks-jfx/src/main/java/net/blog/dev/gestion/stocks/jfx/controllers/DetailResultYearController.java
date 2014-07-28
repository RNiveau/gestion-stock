/**
 * 
 */
package net.blog.dev.gestion.stocks.jfx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import net.blog.dev.gestion.stocks.jfx.utils.FrontUtils;
import net.blog.dev.gestion.stocks.middle.beans.ResultYearBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Kiva
 * 
 */
public class DetailResultYearController implements Initializable {

    static final Logger logger = LoggerFactory.getLogger(DetailResultYearController.class);

    @FXML
	private Label totalInvested;

	@FXML
	private Label totalSold;

	@FXML
	private Label totalRunning;

	@FXML
	private Label totalRunningLastYear;

	@FXML
	private Label budget;

	@FXML
	private Label benefitOnBudget;

	@FXML
	private Label benefitOnBudgetWithTaxes;

    @FXML
    private Label benefitDividendsOnBudget;

    @FXML
    private Label benefitDividendsOnBudgetWithTaxes;

	/*
	 * (non-Javadoc)
	 *
	 * @see net.blog.dev.gestion.stocks.jfx.controllers.IDetailController#
	 * loadFromStockListBean
	 * (net.blog.dev.gestion.stocks.middle.beans.StockListBean)
	 */
	public void loadFromResultYearBean(ResultYearBean bean) {
		totalInvested.setText(FrontUtils.formatPricePercentage(bean
				.getTotalInvested()));
		totalSold
				.setText(FrontUtils.formatPricePercentage(bean.getTotalSold()));
		totalRunning.setText(FrontUtils.formatPricePercentage(bean
				.getTotalRunning()));
		totalRunningLastYear.setText(FrontUtils.formatPricePercentage(bean
				.getTotalRunningPastYear()));
		budget.setText(FrontUtils.formatPricePercentage(bean.getBudget()));
		benefitOnBudget.setText(FrontUtils.formatPricePercentage(bean
				.getBenefitOnBudget()) + " %");
		benefitOnBudgetWithTaxes.setText(FrontUtils.formatPricePercentage(bean
				.getBenefitOnBudgetWithTaxes()) + " %");
        benefitDividendsOnBudget.setText(FrontUtils.formatPricePercentage(bean
                .getBenefitDividendsOnBudget()) + " %");
        benefitDividendsOnBudgetWithTaxes.setText(FrontUtils.formatPricePercentage(bean
                .getBenefitDividendsOnBudgetWithTaxes()) + " %");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
        logger.info("Initialize {} {}", arg0, arg1);
	}

    public Label getBenefitDividendsOnBudget() {
        return benefitDividendsOnBudget;
    }

    public Label getBenefitDividendsOnBudgetWithTaxes() {
        return benefitDividendsOnBudgetWithTaxes;
    }
}
