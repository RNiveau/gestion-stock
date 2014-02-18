/**
 * 
 */
package net.blog.dev.gestion.stocks.jfx.screens;

import javafx.fxml.FXMLLoader;

import javax.inject.Inject;

/**
 * @author Kiva
 * 
 */
public abstract class AbstractScreen {

	@Inject
	private FXMLLoader fxmlLoader;

	/**
	 * @return the fxmlLoader
	 */
	protected FXMLLoader getFxmlLoader() {
		return fxmlLoader;
	}
}
