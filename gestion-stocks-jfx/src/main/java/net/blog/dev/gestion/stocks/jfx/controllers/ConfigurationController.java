/**
 *
 */
package net.blog.dev.gestion.stocks.jfx.controllers;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.blog.dev.gestion.stocks.jfx.IFrontManager;
import net.blog.dev.gestion.stocks.jfx.PoolThreadManager;
import net.blog.dev.gestion.stocks.jfx.screens.LoadingScreen;
import net.blog.dev.gestion.stocks.middle.api.IConfigurationMService;
import net.blog.dev.gestion.stocks.middle.beans.ConfigurationBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;

/**
 * @author Kiva
 */
public class ConfigurationController extends AnchorPane implements
        Initializable {

    static final Logger logger = LoggerFactory.getLogger(ConfigurationController.class);

    @FXML
    private Tooltip tooltipDirectory;

    @FXML
    private TextField srdLoanField;

    @FXML
    private TextField idDropboxField;

    private ConfigurationBean configurationBean = new ConfigurationBean();

    @Inject
    private IConfigurationMService configurationMSservice;

    @Inject
    private IFrontManager frontManager;

    @FXML
    private AnchorPane dropbox;

    @FXML
    private HBox hboxGetDropbox;

    private Stage popUpDropbox;

    @FXML
    private DropboxController dropboxController;

    public void openFileChoose(ActionEvent event) {
        final ConfigurationBean configuration = configurationMSservice
                .getConfiguration();
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(configuration
                .getDirectory()));
        final File directory = directoryChooser.showDialog(frontManager
                .getWindowParent());
        if (directory != null) {
            configurationBean.setDirectory(directory.getAbsolutePath());
        }
        if (StringUtils.isBlank(configurationBean.getIdDropbox()))
            hboxGetDropbox.setVisible(false);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        logger.info("Initialize {} {}", arg0, arg1);
        init();
    }

    private void init() {
        configurationBean = configurationMSservice.getConfiguration();
        tooltipDirectory.setText(configurationBean.getDirectory());
        srdLoanField.setText(configurationBean.getSrdLoan().toString());
        idDropboxField.setText(configurationBean.getIdDropbox());
        idDropboxField.setEditable(false);
    }

    public void save(ActionEvent event) {
        configurationBean.setSrdLoan(Float.parseFloat(srdLoanField.getText()));
        configurationMSservice.setConfiguration(configurationBean);
    }

    public void getDropbox(ActionEvent actionEvent) {
        LoadingScreen loadingScreen = new LoadingScreen();
        final Stage loadingStage = loadingScreen.openLoadingScreen((Stage) frontManager.getWindowParent());

        final ExecutorService excecutor = PoolThreadManager.getPoolThread();
        final Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                logger.debug("In jfx task for getDropbox");
                configurationMSservice.getFromDropbox();
                return null;
            }
        };
        task.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                loadingStage.close();
                logger.error(String.valueOf(task.getException()));
            }
        });
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                loadingStage.close();
            }
        });
        excecutor.execute(task);
    }

    public void openDropbox(ActionEvent actionEvent) {
        if (popUpDropbox == null) {
            popUpDropbox = new Stage();
            popUpDropbox.initModality(Modality.WINDOW_MODAL);
            popUpDropbox.initOwner(frontManager.getWindowParent());
            popUpDropbox.setScene(new Scene(new Group(dropbox)));
        }
        popUpDropbox.show();
        popUpDropbox.addEventHandler(
                CloseController.CLOSE_WINDOW_CLOSE_STOCK, event -> init());
        dropboxController.init();
    }
}
