/**
 * 
 */
package net.blog.dev.gestion.stocks.jfx.utils;

import com.google.common.collect.Iterables;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author Kiva
 * 
 */
public class JfxUtils {

	static public Integer SPACE_LEFT_PANEL = 300;

	static private final String BUNDLE = "net.blog.dev.gestion.stocks.bundles.messages";

    static private final Logger logger = LoggerFactory.getLogger(JfxUtils.class);

	static public Node loadFxml(FXMLLoader loader, String fxml) {
		try {
			loader.setController(null);
			loader.setRoot(null);
            try {
			    loader.setResources(ResourceBundle.getBundle(BUNDLE));
            } catch (MissingResourceException e) {
                logger.warn("Bundle for locale is not available {}, load US file by default", Locale.getDefault());
                loader.setResources(ResourceBundle.getBundle(BUNDLE, Locale.US));
            }
            loader.setLocation(JfxUtils.class.getResource(fxml));

			Node root = null;
			root = (Node) loader.load(JfxUtils.class.getResource(fxml).openStream());
			return root;
		} catch (IOException e) {
			throw new IllegalStateException("cannot load FXML screen", e);
		}
	}

	/**
	 * Charge une nouvelle scene dans la partie droite de l'application
	 * 
	 * @param scene
	 * @param newRoot
	 */
	public static void loadInMainScene(Scene scene, Parent newRoot) {
		if (scene != null && scene.getRoot() != null) {
			final ObservableList<Node> children = scene.getRoot()
					.getChildrenUnmodifiable();
			if (!Iterables.isEmpty(children) && children.size() >= 2) {
				final Node node = children.get(1);
				if (node instanceof VBox) {
					VBox vbox = (VBox) node;
					vbox.getChildren().clear();
					vbox.getChildren().add(newRoot);
				}
			}
		}
	}

}
