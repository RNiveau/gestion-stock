/**
 *
 */
package net.blog.dev.gestion.stocks.jfx;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.blog.dev.gestion.stocks.back.Initialize;
import net.blog.dev.gestion.stocks.core.KWeldContainer;
import net.blog.dev.gestion.stocks.jfx.annotation.Loading;
import net.blog.dev.gestion.stocks.jfx.annotation.Startup;
import net.blog.dev.gestion.stocks.middle.api.IConfigurationMService;
import net.sf.ehcache.CacheManager;
import org.jboss.weld.environment.se.WeldContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.util.AnnotationLiteral;

/**
 * @author Kiva
 */
public class Main extends Application {

    private Stage primaryStage;

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    @SuppressWarnings("serial")
    public void start(Stage primaryStage) {
        logger.debug("Application is starting");
        this.primaryStage = primaryStage;
        // Let's initialize CDI/Weld.
        WeldContainer weldContainer = KWeldContainer.getInstance();
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
        logger.debug("Application started");
    }

    @Override
    public void stop() {
        logger.debug("Application is stopping");
        PoolThreadManager.killPoolThread();
        IConfigurationMService configurationMService = KWeldContainer.getInstance().instance().select(IConfigurationMService.class).get();
        configurationMService.saveOnExit();
        logger.debug("Application stopped");
    }

}
