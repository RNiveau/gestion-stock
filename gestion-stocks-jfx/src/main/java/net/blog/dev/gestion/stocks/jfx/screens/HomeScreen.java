/**
 * 
 */
package net.blog.dev.gestion.stocks.jfx.screens;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import net.blog.dev.gestion.stocks.jfx.FxmlFileConstantes;
import net.blog.dev.gestion.stocks.jfx.IFrontManager;
import net.blog.dev.gestion.stocks.jfx.JfxUtils;
import net.blog.dev.gestion.stocks.jfx.Startup;

/**
 * @author Kiva
 * 
 */
public class HomeScreen extends AbstractScreen {

	@Inject
	private IFrontManager frontManager;

	public void launchJavaFXApplication(@Observes @Startup Stage primaryStage) {
		Parent root = null;
		root = (Parent) JfxUtils.loadFxml(getFxmlLoader(),
				FxmlFileConstantes.HOME);
		primaryStage.setTitle("FXML Welcome");
		primaryStage.setScene(new Scene(root, 1024, 768));
		primaryStage.show();
		frontManager.setWindowParent(primaryStage);
	}
}
