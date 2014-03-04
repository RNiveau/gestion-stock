/**
 * 
 */
package net.blog.dev.gestion.stocks.jfx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import net.blog.dev.gestion.stocks.middle.CalculUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Kiva
 * 
 */
public class CalculHelpController extends AnchorPane implements Initializable {

    static final Logger logger = LoggerFactory.getLogger(CalculHelpController.class);

    @FXML
	private Label atrPourcentage;

	@FXML
	private TextField prixAtr;

	@FXML
	private TextField atr;

	@FXML
	private Label atr1;
	@FXML
	private Label atr15;
	@FXML
	private Label atr2;
	@FXML
	private Label atrm1;
	@FXML
	private Label atrm15;
	@FXML
	private Label atrm2;
	@FXML
	private VBox resultAtr;

	@FXML
	private TextField calculPrice;
	@FXML
	private TextField calculPercentage;
	@FXML
	private Label calculResult;

	@FXML
	private TextField diffPrice1;
	@FXML
	private TextField diffPrice2;
	@FXML
	private Label diffResult;

	public void calculAtr(KeyEvent event) {
		if (StringUtils.isNotBlank(atr.getText())
				&& StringUtils.isNotBlank(prixAtr.getText())) {
			final float fAtr = Float.parseFloat(atr.getText());
			final float prix = Float.parseFloat(prixAtr.getText());
			atrPourcentage.setText(CalculUtils.getPercentageIntoValues(fAtr,
					prix).toString());
			atr1.setText((prix + fAtr) + "");
			atr15.setText((prix + 1.5 * fAtr) + "");
			atr2.setText((prix + 2 * fAtr) + "");
			atrm1.setText((prix - fAtr) + "");
			atrm15.setText((prix - 1.5 * fAtr) + "");
			atrm2.setText((prix - 2 * fAtr) + "");
			resultAtr.setVisible(true);
		}
	}

	public void calculPercentage(KeyEvent event) {
		if (StringUtils.isNotBlank(calculPrice.getText())
				&& StringUtils.isNotBlank(calculPercentage.getText())) {
			final float price = Float.parseFloat(calculPrice.getText());
			final float percentage = Float.parseFloat(calculPercentage
					.getText());
			calculResult.setText(CalculUtils.addPercentage(price, percentage)
					.toString());
		}
	}

	public void calculDiff(KeyEvent event) {
		if (StringUtils.isNotBlank(diffPrice1.getText())
				&& StringUtils.isNotBlank(diffPrice2.getText())) {
			final float price1 = Float.parseFloat(diffPrice1.getText());
			final float price2 = Float.parseFloat(diffPrice2.getText());
			diffResult.setText(CalculUtils.getPercentageBetweenTwoValues(
					price1, price2).toString());
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
        logger.info("Initialize {} {}", arg0, arg1);
		resultAtr.managedProperty().bind(resultAtr.visibleProperty());
	}

}
