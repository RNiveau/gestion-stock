package net.blog.dev.gestion.stocks.jfx.controllers;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.blog.dev.gestion.stocks.jfx.PoolThreadManager;
import net.blog.dev.gestion.stocks.jfx.screens.LoadingScreen;
import net.blog.dev.gestion.stocks.middle.api.IConfigurationMService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;

public class CheckDropBoxFileController extends AnchorPane implements Initializable {

    static final Logger logger = LoggerFactory.getLogger(CheckDropBoxFileController.class);

    static public final EventType<WindowEvent> CLOSE_WINDOW_DROPBOX = new EventType<>("CLOSE_WINDOW_DROPBOX");

    @Inject
    private IConfigurationMService configurationMService;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        logger.info("Initialize {} {}", arg0, arg1);
    }

    public void ok(ActionEvent event) {
        LoadingScreen loadingScreen = new LoadingScreen();
        final Stage loadingStage = loadingScreen.openLoadingScreen(((Button)event.getSource()).getScene().getWindow());
        final ExecutorService excecutor = PoolThreadManager.getPoolThread();
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                configurationMService.getFromDropbox();
                return null;
            }
        };
        task.setOnSucceeded((e) -> {
            loadingStage.close();
            close(event);
        });
        excecutor.submit(task);
    }

    public void ko(ActionEvent event) {
        close(event);
    }

    private void close(ActionEvent event) {
        ((Button)event.getSource()).getScene()
                .getWindow()
                .fireEvent(
                        new WindowEvent(((Button)event.getSource()).getScene().getWindow(),
                                CLOSE_WINDOW_DROPBOX));
        ((Stage) ((Button)event.getSource()).getScene().getWindow()).close();
    }
}
