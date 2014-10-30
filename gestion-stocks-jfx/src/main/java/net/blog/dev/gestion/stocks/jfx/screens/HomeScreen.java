/**
 *
 */
package net.blog.dev.gestion.stocks.jfx.screens;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.blog.dev.gestion.stocks.jfx.IFrontManager;
import net.blog.dev.gestion.stocks.jfx.PoolThreadManager;
import net.blog.dev.gestion.stocks.jfx.annotation.Startup;
import net.blog.dev.gestion.stocks.jfx.controllers.CheckDropBoxFileController;
import net.blog.dev.gestion.stocks.jfx.fxml.FxmlFileConstantes;
import net.blog.dev.gestion.stocks.jfx.utils.JfxUtils;
import net.blog.dev.gestion.stocks.middle.api.IConfigurationMService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.concurrent.ExecutorService;

/**
 * @author Kiva
 */
public class HomeScreen extends AbstractScreen {

    static final Logger logger = LoggerFactory.getLogger(HomeScreen.class);

    @Inject
    private IConfigurationMService configurationMService;

    @Inject
    private FXMLLoader loader;

    @Inject
    private IFrontManager frontManager;

    public void launchJavaFXApplication(@Observes @Startup Stage primaryStage) {
        frontManager.setWindowParent(primaryStage);
        Parent root = null;
        root = (Parent) JfxUtils.loadFxml(getFxmlLoader(),
                FxmlFileConstantes.HOME);
        primaryStage.setTitle("FXML Welcome");
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.show();

        LoadingScreen loadingScreen = new LoadingScreen();
        final Stage loadingStage = loadingScreen.openLoadingScreen(primaryStage);
        final ExecutorService excecutor = PoolThreadManager.getPoolThread();
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return configurationMService.newerFileExist();
            }
        };
        task.setOnSucceeded((event) -> {
            if ((Boolean) event.getSource().getValue()) {
                logger.debug("A newer dropbox file exist");
                final Stage dropbox = new Stage();
                dropbox.initModality(Modality.WINDOW_MODAL);
                dropbox.initOwner(loadingStage);
                dropbox.setScene(new Scene(new Group(JfxUtils.loadFxml(loader, FxmlFileConstantes.LOAD_FROM_DROPBOX))));
                dropbox.show();
                dropbox.addEventHandler(
                        CheckDropBoxFileController.CLOSE_WINDOW_DROPBOX,
                        windowEvent -> {
                            loadingStage.close();
                        });

            } else {
                loadingStage.close();
            }
        });
        excecutor.execute(task);

    }
}
