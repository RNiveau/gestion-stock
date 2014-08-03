/**
 *
 */
package net.blog.dev.gestion.stocks.jfx.screens;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.blog.dev.gestion.stocks.jfx.IFrontManager;
import net.blog.dev.gestion.stocks.jfx.annotation.Loading;
import net.blog.dev.gestion.stocks.jfx.fxml.FxmlFileConstantes;
import net.blog.dev.gestion.stocks.jfx.utils.JfxUtils;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 * @author Kiva
 */
public class LoadingScreen extends AbstractScreen {

    @Inject
    private IFrontManager frontManager;

    public void openLoadingScreen(@Observes @Loading Stage primaryStage) {
        Stage loading = new Stage();
        loading.initModality(Modality.WINDOW_MODAL);
        loading.initOwner(frontManager.getWindowParent());
        loading.setScene(new Scene(new Group(JfxUtils.loadFxml(getFxmlLoader(), FxmlFileConstantes.LOADING))));
        loading.show();
    }
}
