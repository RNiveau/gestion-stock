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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import net.blog.dev.gestion.stocks.jfx.FxmlFileConstantes;
import net.blog.dev.gestion.stocks.jfx.JfxUtils;
import net.blog.dev.gestion.stocks.middle.api.IAddMovementMService;
import net.blog.dev.gestion.stocks.middle.beans.AddMovementBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class AddMovementController implements Initializable {

    static final Logger logger = LoggerFactory.getLogger(AddMovementController.class);

	@FXML
	private TextArea comment;
	@FXML
	private TextField total;
	@FXML
	private TextField date;
	@FXML
	private ChoiceBox<String> provision;
	@FXML
	private ChoiceBox<String> account;

	@FXML
	private Label errorComment;
	@FXML
	private Label errorTotal;
	@FXML
	private Label errorDate;
	@FXML
	private Label errorAccount;
	@FXML
	private Label errorProvision;

	static String PROVISION_CREDIT = "Credit";
	static String PROVISION_RETRAIT = "Retrait";

	@Inject
	private IAddMovementMService addMovementMService;

	@Inject
	private FXMLLoader loader;

	private ResourceBundle bundle;

	private Map<String, String> accountMap;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
        logger.info("Initialize {} {}", arg0, arg1);
		bundle = arg1;
		accountMap = new HashMap<String, String>();
		buildProvisionBox();
		fillChoiseBox(account, addMovementMService.getListAccount(), accountMap);
	}

	private void buildProvisionBox() {
		ObservableList<String> observableArrayList = FXCollections
				.observableArrayList();
		observableArrayList.add(PROVISION_CREDIT);
		observableArrayList.add(PROVISION_RETRAIT);
		provision.setItems(observableArrayList);
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
		AddMovementBean addMovementBean = new AddMovementBean();
		addMovementBean.setTotal(total.getText());
		addMovementBean.setDate(date.getText());
		addMovementBean.setComment(comment.getText());
		addMovementBean.setProvision(StringUtils.equals(provision.getValue(),
				PROVISION_CREDIT));
		addMovementBean.setAccount(accountMap.get(account.getValue()));
		Map<String, String> errors = addMovementMService
				.validate(addMovementBean);
		if (errors != null && !errors.isEmpty()) {
			errorAccount.setText(errors.get("account") != null ? bundle
					.getString(errors.get("account")) : null);
			errorDate.setText(errors.get("date") != null ? bundle
					.getString(errors.get("date")) : null);
			errorTotal.setText(errors.get("total") != null ? bundle
					.getString(errors.get("total")) : null);
		} else {
			addMovementMService.addMovement(addMovementBean);
			final Node newRoot = JfxUtils.loadFxml(loader,
					FxmlFileConstantes.LIST_MOVEMENTS);
			JfxUtils.loadInMainScene(((Node) event.getSource()).getScene(),
					(Parent) newRoot);
		}
	}
}
