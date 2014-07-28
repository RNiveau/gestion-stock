package net.blog.dev.gestion.stocks.jfx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import net.blog.dev.gestion.stocks.jfx.fxml.FxmlFileConstantes;
import net.blog.dev.gestion.stocks.jfx.utils.JfxUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController extends AnchorPane implements Initializable {

    static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @FXML
    private VBox leftPanel;

    @FXML
    private VBox rightPanel;

    @Inject
    private FXMLLoader loader;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        logger.info("Initialize {} {}", arg0, arg1);
        leftPanel.setPrefHeight(1024);
    }

    public void openStocksListRunning(ActionEvent event) {
        loadRightPane(FxmlFileConstantes.LIST_STOCKS_RUNNING);
    }

    public void openStocksListClose(ActionEvent event) {
        loadRightPane(FxmlFileConstantes.LIST_STOCKS_CLOSE);
    }

    public void openAddStock(ActionEvent event) {
        loadRightPane(FxmlFileConstantes.ADD_STOCK);
    }

    public void openHelp(ActionEvent event) {
        loadRightPane(FxmlFileConstantes.CALCUL_HELP);
    }

    public void openConfiguration(ActionEvent event) {
        loadRightPane(FxmlFileConstantes.CONFIGURATION);
    }

    public void openResultYear(ActionEvent event) {
        loadRightPane(FxmlFileConstantes.RESULT_YEAR);
    }

    public void openAddMovement(ActionEvent event) {
        loadRightPane(FxmlFileConstantes.ADD_MOVEMENT);
    }

    public void openListMovement(ActionEvent event) {
        loadRightPane(FxmlFileConstantes.LIST_MOVEMENTS);
    }

    public void openCompareStock(ActionEvent event) {
        loadRightPane(FxmlFileConstantes.COMPARE_STOCK);
    }

    private void loadRightPane(String fxml) {
        logger.info("Go to {}", fxml);
        Node root = JfxUtils.loadFxml(loader, fxml);
        rightPanel.getChildren().clear();
        rightPanel.getChildren().add(root);
    }
}
