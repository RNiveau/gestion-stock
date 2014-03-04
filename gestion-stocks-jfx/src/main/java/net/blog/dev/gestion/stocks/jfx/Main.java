/**
 * 
 */
package net.blog.dev.gestion.stocks.jfx;

import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;
import net.blog.dev.gestion.stocks.back.Initialize;
import net.sf.ehcache.CacheManager;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import javax.enterprise.util.AnnotationLiteral;

/**
 * @author Kiva
 * 
 */
public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	@SuppressWarnings("serial")
	public void start(Stage primaryStage) {
		// Let's initialize CDI/Weld.
        WeldContainer weldContainer = new Weld().initialize();
        CacheManager.create();

		// Make the application parameters injectable with a standard CDI
		// annotation
		weldContainer.instance().select(ApplicationParametersProvider.class)
				.get().setParameters(getParameters());
		// Now that JavaFX thread is ready
		// let's inform whoever cares using standard CDI notification mechanism:
		// CDI events
		weldContainer.event()
				.select(String.class, new AnnotationLiteral<Initialize>() {
				}).fire("");
		weldContainer.event()
				.select(Stage.class, new AnnotationLiteral<Startup>() {
				}).fire(primaryStage);
		primaryStage.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
		primaryStage.setHeight(Screen.getPrimary().getVisualBounds()
				.getHeight());
		primaryStage.setX(Screen.getPrimary().getVisualBounds().getMinX());
		primaryStage.setY(Screen.getPrimary().getVisualBounds().getMinY());
		primaryStage.setTitle("KStocks");
	}

}
