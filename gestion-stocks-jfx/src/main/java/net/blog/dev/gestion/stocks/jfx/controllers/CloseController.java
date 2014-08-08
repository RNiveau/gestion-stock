/**
 * 
 */
package net.blog.dev.gestion.stocks.jfx.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.blog.dev.gestion.stocks.middle.Utils;
import net.blog.dev.gestion.stocks.middle.api.ICloseStockMService;
import net.blog.dev.gestion.stocks.middle.beans.CloseBean;
import net.blog.dev.gestion.stocks.middle.beans.DetailStockBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Kiva
 * 
 */
public class CloseController extends AnchorPane implements Initializable {

    static final Logger logger = LoggerFactory.getLogger(CloseController.class);

	@Inject
	private ICloseStockMService closeStockMService;

	@Inject
	private FXMLLoader loader;

	@FXML
	private TextField price;

	@FXML
	private Label errorPrice;

	@FXML
	private TextField taxes;

	@FXML
	private Label errorTaxes;

	@FXML
	private TextField date;

	@FXML
	private Label errorDate;

	private ResourceBundle bundle;

	DetailStockBean detailStockBean;

	static public final EventType<WindowEvent> CLOSE_WINDOW_CLOSE_STOCK = new EventType<>("CLOSE_WINDOW_CLOSE_STOCK");

	public void closeStock(ActionEvent event) {
		final CloseBean closeBean = new CloseBean();
		closeBean.setDate(date.getText());
		closeBean.setTaxes(taxes.getText());
		closeBean.setPrice(price.getText());
		closeBean.setId(detailStockBean.getId());
		Map<String, String> errors = closeStockMService.validate(closeBean);
		if (errors != null && !errors.isEmpty()) {
			errorPrice.setText(errors.get("price") != null ? bundle
					.getString(errors.get("price")) : null);
			errorPrice.setVisible(errors.get("price") != null);

			errorTaxes.setText(errors.get("taxes") != null ? bundle
					.getString(errors.get("taxes")) : null);
			errorTaxes.setVisible(errors.get("taxes") != null);

			errorDate.setText(errors.get("date") != null ? bundle
					.getString(errors.get("date")) : null);
			errorDate.setVisible(errors.get("date") != null);
		} else {
			closeStockMService.closeStock(closeBean);
			price.getScene()
					.getWindow()
					.fireEvent(
							new WindowEvent(price.getScene().getWindow(),
									CloseController.CLOSE_WINDOW_CLOSE_STOCK));
			((Stage) price.getScene().getWindow()).close();
		}
	}

	public void init(DetailStockBean detailStockBean) {
        logger.info("init {}", detailStockBean);
		this.detailStockBean = detailStockBean;
		date.setText(Utils.formatDate(new Date(), "dd/MM/yyyy"));
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
        logger.info("Initialize {} {}", arg0, arg1);
		bundle = arg1;
		errorPrice.managedProperty().bind(errorPrice.visibleProperty());
		errorDate.managedProperty().bind(errorDate.visibleProperty());
		errorTaxes.managedProperty().bind(errorTaxes.visibleProperty());
	}
}
