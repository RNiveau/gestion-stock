package net.blog.dev.gestion.stocks.jfx.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import javax.inject.Inject;

import net.blog.dev.gestion.stocks.middle.api.IResultYearMService;
import net.blog.dev.gestion.stocks.middle.beans.ResultYearBean;

public class ResultYearController extends AnchorPane implements Initializable {

	@FXML
	private TableView<ResultYearBean> table;

	@Inject
	private IResultYearMService resultYearMService;

	private Tooltip tooltip;

	@FXML
	private Pane popupDetail;

	@FXML
	private DetailResultYearController popupDetailController;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<ResultYearBean> list = FXCollections
				.observableArrayList();
		list.addAll(resultYearMService.getResultList());
		table.setItems(list);
	}

	public void openDetail(MouseEvent event) {
		if (event.getButton().equals(MouseButton.SECONDARY)) {
			if (tooltip == null) {
				tooltip = new Tooltip();
			}
			tooltip.show(table, event.getScreenX(), event.getScreenY());
			if (popupDetailController != null) {
				popupDetailController.loadFromResultYearBean(table
						.getSelectionModel().getSelectedItem());
				setTooltipContent(popupDetail);
			}
		}
	}

	public void setTooltipContent(Node content) {
		final Group root = (Group) tooltip.getScene().getRoot();
		root.getChildren().clear();
		root.getChildren().add(content);
	}

}
