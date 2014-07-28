/**
 * 
 */
package net.blog.dev.gestion.stocks.jfx.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import net.blog.dev.gestion.stocks.jfx.IFrontManager;
import net.blog.dev.gestion.stocks.jfx.JfxUtils;
import net.blog.dev.gestion.stocks.jfx.TwoFloatValueFactory;
import net.blog.dev.gestion.stocks.middle.api.IStocksListMService;
import net.blog.dev.gestion.stocks.middle.beans.StockListBean;
import net.blog.dev.gestion.stocks.middle.beans.StockListCloseBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

import static net.blog.dev.gestion.stocks.jfx.FrontUtils.formatDate;
import static net.blog.dev.gestion.stocks.jfx.FrontUtils.formatStringToDate;

/**
 * @author Kiva
 * 
 */
public class StocksListCloseController extends ScrollPane implements
		Initializable {

    static final Logger logger = LoggerFactory.getLogger(StocksListCloseController.class);

	@Inject
	private IStocksListMService stocksListMService;

	@Inject
	private IFrontManager frontManager;

	@FXML
	private VBox tableListStock;

	@FXML
	private StockListTableController tableListStockController;

	@FXML
	private Pane popupDetail;

	@FXML
	private ScrollPane scrollPane;

	@FXML
	private DetailStockCloseController popupDetailController;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
        logger.info("Initialize {} {}", arg0, arg1);
		scrollPane.setPrefWidth(frontManager.getWindowParent().getWidth()
				- JfxUtils.SPACE_LEFT_PANEL);

		ObservableList<StockListBean> list = FXCollections
				.observableArrayList();
		list.addAll(stocksListMService.getStocksListClose());
		final TableColumn<StockListBean, String> columnDate = new TableColumn<StockListBean, String>();
		final PropertyValueFactory propertyDate = new PropertyValueFactory(
				"sellDate");
		columnDate.setCellValueFactory(propertyDate);
		columnDate.setText("Date de vente");
		columnDate.setPrefWidth(100);
        columnDate.setComparator((o1, o2) -> formatStringToDate(o1, "dd/MM/yyyy").compareTo(formatStringToDate(o2, "dd/MM/yyyy")));
        columnDate.setCellValueFactory(value -> new SimpleStringProperty(formatDate(((StockListCloseBean)value.getValue()).getSellDate(), "dd/MM/yyyy")));

        tableListStockController.getStocksList().getColumns().add(columnDate);

		final TableColumn<StockListBean, String> columnPrice = new TableColumn<StockListBean, String>();
		final PropertyValueFactory propertyPrice = new PropertyValueFactory(
				"sellPrice");
		columnPrice.setCellValueFactory(propertyPrice);
		columnPrice.setText("Prix total vente");
		columnPrice.setPrefWidth(100);
		tableListStockController.getStocksList().getColumns().add(columnPrice);

		final TableColumn<StockListBean, String> columnUnitPrice = new TableColumn<StockListBean, String>();
		final PropertyValueFactory propertyUnitPrice = new PropertyValueFactory(
				"sellUnitPrice");
		columnUnitPrice.setCellValueFactory(propertyUnitPrice);
		columnUnitPrice.setText("Prix unitaire vente");
		columnUnitPrice.setPrefWidth(100);
		tableListStockController.getStocksList().getColumns()
				.add(columnUnitPrice);

		final TableColumn<StockListBean, String> columnGain = new TableColumn<StockListBean, String>();
		final TwoFloatValueFactory propertyGain = new TwoFloatValueFactory();
		propertyGain.setProperty("gain");
		propertyGain.setProperty2("gainPercentage");
		columnGain.setCellValueFactory(propertyGain);
		columnGain.setText("Gain brut (%)");
		columnGain.setPrefWidth(150);
		tableListStockController.getStocksList().getColumns().add(columnGain);

		final TableColumn<StockListBean, String> columnGainLessTaxes = new TableColumn<StockListBean, String>();
		final TwoFloatValueFactory propertyGainLessTaxes = new TwoFloatValueFactory();
		propertyGainLessTaxes.setProperty("gainLessTaxes");
		propertyGainLessTaxes.setProperty2("gainLessTaxesPercentage");
		columnGainLessTaxes.setCellValueFactory(propertyGainLessTaxes);
		columnGainLessTaxes.setText("Gain net (%)");
		columnGainLessTaxes.setPrefWidth(150);
		tableListStockController.getStocksList().getColumns()
				.add(columnGainLessTaxes);

		tableListStockController.getStocksList().setItems(list);
		tableListStockController.setDetail(popupDetail);
		tableListStockController
				.setDetailStockController(popupDetailController);
		scrollPane
				.setPrefHeight(frontManager.getWindowParent().getHeight() - 100);
		tableListStockController.getStocksList().setPrefHeight(
				frontManager.getWindowParent().getHeight() - 120);
	}
}
