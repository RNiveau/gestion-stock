/**
 * 
 */
package net.blog.dev.gestion.stocks.jfx.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.blog.dev.gestion.stocks.jfx.FrontUtils;
import net.blog.dev.gestion.stocks.middle.beans.DetailStockBean.StopGain;
import net.blog.dev.gestion.stocks.middle.beans.StockListBean;

/**
 * @author Kiva
 * 
 */
public class DetailStockRunningController extends AbstractDetailController {

	@FXML
	private Label taxes;

	@FXML
	private Label gain1atr;
	@FXML
	private Label gain15atr;
	@FXML
	private Label gain2atr;
	@FXML
	private Label stop1atr;
	@FXML
	private Label stop15atr;
	@FXML
	private Label stop2atr;
	@FXML
	private Label account;
	@FXML
	private Label direction;
	@FXML
	private Label type;
	@FXML
	private Label strategy;
	@FXML
	private HBox hboxSrd;
	@FXML
	private Label srd;

	@FXML
	private AnchorPane closeStock;

	private Stage popUpCloseStock;

	@FXML
	private CloseController closeStockController;

	@FXML
	private AnchorPane addDividend;

	@FXML
	private AddDividendController addDividendController;

	private Stage popUpAddDividend;

	@FXML
	private AnchorPane showDividend;

	@FXML
	private ShowDividendController showDividendController;

	public void loadFromStockListBean(StockListBean bean) {
		setDetailStockBean(getDetailStockMService().getDetailStockBean(bean));
		taxes.setText(getDetailStockBean().getTaxes().toString());
		strategy.setText(getBundle().getString(
				getDetailStockBean().getStrategy()));
		account.setText(getBundle()
				.getString(getDetailStockBean().getAccount()));
		type.setText(getBundle().getString(getDetailStockBean().getType()));
		direction.setText(getBundle().getString(
				getDetailStockBean().getDirection()));
		hboxSrd.setVisible(getDetailStockBean().getEstimateSrd() != null);
		if (getDetailStockBean().getEstimateSrd() != null) {
			srd.setText(getDetailStockBean().getEstimateSrd().toString());
		}
		if (getDetailStockBean().getStopGain() != null
				&& getDetailStockBean().getStopGain().size() == 6) {
			generateLabelAtr(
					gain1atr,
					getDetailStockBean().getStopGain().get(
							getDetailStockBean().GAIN_ATR_1));
			generateLabelAtr(
					gain15atr,
					getDetailStockBean().getStopGain().get(
							getDetailStockBean().GAIN_ATR_15));
			generateLabelAtr(
					gain2atr,
					getDetailStockBean().getStopGain().get(
							getDetailStockBean().GAIN_ATR_2));
			generateLabelAtr(
					stop1atr,
					getDetailStockBean().getStopGain().get(
							getDetailStockBean().STOP_ATR_1));
			generateLabelAtr(
					stop15atr,
					getDetailStockBean().getStopGain().get(
							getDetailStockBean().STOP_ATR_15));
			generateLabelAtr(
					stop2atr,
					getDetailStockBean().getStopGain().get(
							getDetailStockBean().STOP_ATR_2));
		}
	}

	public void closeStock(ActionEvent event) {
		if (popUpCloseStock == null) {
			popUpCloseStock = new Stage();
			popUpCloseStock.initModality(Modality.WINDOW_MODAL);
			popUpCloseStock.initOwner(getFrontManager().getWindowParent());
			popUpCloseStock.setScene(new Scene(new Group(closeStock)));
		}
		popUpCloseStock.show();
		popUpCloseStock.addEventHandler(
				CloseController.CLOSE_WINDOW_CLOSE_STOCK,
				new EventHandler<Event>() {

					@Override
					public void handle(Event event) {
						((Tooltip) strategy.getScene().getWindow())
								.fireEvent(new WindowEvent(
										strategy.getScene().getWindow(),
										CloseController.CLOSE_WINDOW_CLOSE_STOCK));
						((Tooltip) strategy.getScene().getWindow()).hide();
					}
				});
		closeStockController.init(getDetailStockBean());
	}

	public void addDividend(ActionEvent event) {
		if (popUpAddDividend == null) {
			popUpAddDividend = new Stage();
			popUpAddDividend.initModality(Modality.WINDOW_MODAL);
			popUpAddDividend.initOwner(getFrontManager().getWindowParent());
			popUpAddDividend.setScene(new Scene(new Group(addDividend)));
		}
		popUpAddDividend.show();
		addDividendController.init(getDetailStockBean());
	}

	private void generateLabelAtr(Label atr, StopGain gain) {
		atr.setText(FrontUtils.formatPricePercentage(gain.getPrice()) + " ("
				+ FrontUtils.formatPricePercentage(gain.getPercentage())
				+ " %)");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setShowDividend(showDividend);
		setShowDividendController(showDividendController);
		super.initialize(arg0, arg1);
		hboxSrd.managedProperty().bind(hboxSrd.visibleProperty());
	}

}
