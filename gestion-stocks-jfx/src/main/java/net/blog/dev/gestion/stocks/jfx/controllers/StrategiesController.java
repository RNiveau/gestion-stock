/**
 *
 */
package net.blog.dev.gestion.stocks.jfx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import net.blog.dev.gestion.stocks.jfx.utils.FrontUtils;
import net.blog.dev.gestion.stocks.jfx.utils.JfxUtils;
import net.blog.dev.gestion.stocks.middle.api.IAddStockMService;
import net.blog.dev.gestion.stocks.middle.api.IStrategiesMService;
import net.blog.dev.gestion.stocks.middle.beans.StrategyBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Kiva
 */
public class StrategiesController implements Initializable {

    static final Logger logger = LoggerFactory.getLogger(StrategiesController.class);

    @FXML
    public ChoiceBox strategies;

    @FXML
    public Label nbrBuy;

    @FXML
    public Label nbrSell;

    @FXML
    public Label durationBuy;

    @FXML
    public Label durationSell;

    @FXML
    public Label benefitsBuy;

    @FXML
    public Label benefitsSell;

    @FXML
    public Label dividends;

    @FXML
    public Label benefitsAverageBuy;

    @FXML
    public Label benefitsAverageSell;

    @FXML
    public Label positionRunning;

    @FXML
    public Label moneyRunning;

    @Inject
    private IStrategiesMService strategiesMService;

    @Inject
    private IAddStockMService addStockMService;

    private Map<String, String> strategiesMap;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        logger.info("Initialize {} {}", arg0, arg1);
        strategiesMap = new HashMap<String, String>();
        JfxUtils.fillChoiseBox(strategies, addStockMService.getListStrategies(),
                strategiesMap, arg1);
    }


    public void run(ActionEvent event) {
        StrategyBean strategy = strategiesMService.getStrategy(strategiesMap.get(strategies.getValue()));
        benefitsBuy.setText(strategy.getBenefitsBuy().toString());
        nbrBuy.setText(strategy.getNbrBuy().toString());
        nbrSell.setText(strategy.getNbrSell().toString());
        benefitsBuy.setText(FrontUtils.formatPricePercentage(strategy.getBenefitsBuy()));
        benefitsSell.setText(FrontUtils.formatPricePercentage(strategy.getBenefitsSell()));
        benefitsAverageBuy.setText(FrontUtils.formatPricePercentage(strategy.getBenefitsAverageBuy()));
        benefitsAverageSell.setText(FrontUtils.formatPricePercentage(strategy.getBenefitsAverageSell()));
        dividends.setText(FrontUtils.formatPricePercentage(strategy.getDividends()));
        positionRunning.setText(strategy.getPositionRunning().toString());
        moneyRunning.setText(FrontUtils.formatPricePercentage(strategy.getMoneyRunning()));
        durationBuy.setText(Integer.toString((int) strategy.getDurationBuy().toDays()));
        durationSell.setText(Integer.toString((int) strategy.getDurationSell().toDays()));
    }
}
