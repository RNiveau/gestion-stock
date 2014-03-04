package net.blog.dev.gestion.stocks.jfx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import net.blog.dev.gestion.stocks.middle.api.IMovementsListMService;
import net.blog.dev.gestion.stocks.middle.beans.MovementListBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MovementsListController extends AnchorPane implements
		Initializable {

    static final Logger logger = LoggerFactory.getLogger(MovementsListController.class);

	@FXML
	private TableView<MovementListBean> table;

	@Inject
	private IMovementsListMService movementsListMService;

	@Override
	public void initialize(URL arg0, ResourceBundle bundle) {
        logger.info("Initialize {} {}", arg0, bundle);
		ObservableList<MovementListBean> list = FXCollections
				.observableArrayList();
		final List<MovementListBean> movementsList = movementsListMService
				.getMovementsList();
		for (MovementListBean bean : movementsList)
			bean.setAccount(bundle.getString(bean.getAccount()));
		list.addAll(movementsList);
		table.setItems(list);
	}
}
