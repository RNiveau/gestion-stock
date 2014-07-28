package net.blog.dev.gestion.stocks.jfx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import net.blog.dev.gestion.stocks.jfx.fxml.FxmlFileConstantes;
import net.blog.dev.gestion.stocks.jfx.utils.JfxUtils;
import net.blog.dev.gestion.stocks.middle.api.IAddStockMService;
import net.blog.dev.gestion.stocks.middle.beans.AddStockBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class AddStockController implements Initializable {

    static final Logger logger = LoggerFactory.getLogger(AddStockController.class);

	@FXML
	private TextField stockName;
	@FXML
	private TextField stockCode;
	@FXML
	private TextField quantity;
	@FXML
	private TextField price;
	@FXML
	private TextField taxes;
	@FXML
	private TextField buyDate;
	@FXML
	private TextField atr;
	@FXML
	private ChoiceBox<String> type;
	@FXML
	private ChoiceBox<String> direction;
	@FXML
	private ChoiceBox<String> account;
	@FXML
	private ChoiceBox<String> strategy;

	@FXML
	private Label errorStockName;
	@FXML
	private Label errorStockCode;
	@FXML
	private Label errorQuantity;
	@FXML
	private Label errorPrice;
	@FXML
	private Label errorTaxes;
	@FXML
	private Label errorBuyDate;
	@FXML
	private Label errorAtr;
	@FXML
	private Label errorType;
	@FXML
	private Label errorDirection;
	@FXML
	private Label errorAccount;
	@FXML
	private Label errorStrategy;

	@Inject
	private IAddStockMService addStockMService;

	@Inject
	private FXMLLoader loader;

	private ResourceBundle bundle;

	private Map<String, String> strategiesMap;
	private Map<String, String> accountMap;
	private Map<String, String> directionMap;
	private Map<String, String> typeMap;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
        logger.info("Initialize {} {}", arg0, arg1);
		bundle = arg1;
		strategiesMap = new HashMap<String, String>();
		typeMap = new HashMap<String, String>();
		directionMap = new HashMap<String, String>();
		accountMap = new HashMap<String, String>();
		fillChoiseBox(strategy, addStockMService.getListStrategies(),
				strategiesMap);
		fillChoiseBox(type, addStockMService.getListOrderType(), typeMap);
		fillChoiseBox(direction, addStockMService.getListDirection(),
				directionMap);
		fillChoiseBox(account, addStockMService.getListAccount(), accountMap);
	}

	private void fillChoiseBox(ChoiceBox<String> choiseBox,
			List<String> listChoise, Map<String, String> mapChoise) {
		ObservableList<String> observableArrayList = FXCollections
				.observableArrayList();
		for (String choise : listChoise) {
			mapChoise.put(bundle.getString(choise), choise);
			observableArrayList.add(bundle.getString(choise));
		}
		choiseBox.setItems(observableArrayList);
	}

	public void addStock(ActionEvent event) {
		AddStockBean addStockBean = new AddStockBean();
		addStockBean.setName(stockName.getText());
		addStockBean.setCode(stockCode.getText());
		addStockBean.setQuantity(quantity.getText());
		addStockBean.setPrice(price.getText());
		addStockBean.setTaxes(taxes.getText());
		addStockBean.setDate(buyDate.getText());
		addStockBean.setAtr(atr.getText());
		addStockBean.setTypeOrder(typeMap.get(type.getValue()));
		addStockBean.setDirection(directionMap.get(direction.getValue()));
		addStockBean.setAccount(accountMap.get(account.getValue()));
		addStockBean.setStrategy(strategiesMap.get(strategy.getValue()));
		Map<String, String> errors = addStockMService.validate(addStockBean);
		if (errors != null && !errors.isEmpty()) {
			errorAccount.setText(errors.get("account") != null ? bundle
					.getString(errors.get("account")) : null);
			errorAtr.setText(errors.get("atr") != null ? bundle
					.getString(errors.get("atr")) : null);
			errorBuyDate.setText(errors.get("date") != null ? bundle
					.getString(errors.get("date")) : null);
			errorDirection.setText(errors.get("direction") != null ? bundle
					.getString(errors.get("direction")) : null);
			errorQuantity.setText(errors.get("quantity") != null ? bundle
					.getString(errors.get("quantity")) : null);
			errorPrice.setText(errors.get("price") != null ? bundle
					.getString(errors.get("price")) : null);
			errorStockCode.setText(errors.get("code") != null ? bundle
					.getString(errors.get("code")) : null);
			errorStockName.setText(errors.get("name") != null ? bundle
					.getString(errors.get("name")) : null);
			errorTaxes.setText(errors.get("taxes") != null ? bundle
					.getString(errors.get("taxes")) : null);
			errorType.setText(errors.get("typeOrder") != null ? bundle
					.getString(errors.get("typeOrder")) : null);
			errorStrategy.setText(errors.get("strategy") != null ? bundle
					.getString(errors.get("strategy")) : null);

		} else {
			addStockMService.addStock(addStockBean);
			final Node newRoot = JfxUtils.loadFxml(loader,
					FxmlFileConstantes.LIST_STOCKS_RUNNING);
			JfxUtils.loadInMainScene(((Node) event.getSource()).getScene(),
					(Parent) newRoot);
		}
	}
}
