/**
 * 
 */
package net.blog.dev.gestion.stocks.jfx.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.inject.Inject;

import net.blog.dev.gestion.stocks.jfx.IFrontManager;
import net.blog.dev.gestion.stocks.middle.api.IDetailStockMService;
import net.blog.dev.gestion.stocks.middle.beans.DetailStockBean;

/**
 * @author Kiva
 * 
 */
public abstract class AbstractDetailController extends Pane implements
		Initializable, IDetailController {

	private AnchorPane showDividend;

	private ShowDividendController showDividendController;

	private Stage popUpShowDividend;

	private ResourceBundle bundle;

	private DetailStockBean detailStockBean;

	@Inject
	private IDetailStockMService detailStockMService;

	@Inject
	private FXMLLoader loader;

	@Inject
	private IFrontManager frontManager;

	public void showDividendList(ActionEvent event) {
		if (popUpShowDividend == null) {
			popUpShowDividend = new Stage();
			popUpShowDividend.initModality(Modality.WINDOW_MODAL);
			popUpShowDividend.initOwner(frontManager.getWindowParent());
			popUpShowDividend.setScene(new Scene(new Group(showDividend)));
		}
		popUpShowDividend.show();
		showDividendController.init(getDetailStockBean());
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		bundle = arg1;
	}

	/**
	 * @return the detailStockBean
	 */
	public DetailStockBean getDetailStockBean() {
		return detailStockBean;
	}

	/**
	 * @param detailStockBean
	 *            the detailStockBean to set
	 */
	public void setDetailStockBean(DetailStockBean detailStockBean) {
		this.detailStockBean = detailStockBean;
	}

	/**
	 * @return the popUpShowDividend
	 */
	protected Stage getPopUpShowDividend() {
		return popUpShowDividend;
	}

	/**
	 * @param popUpShowDividend
	 *            the popUpShowDividend to set
	 */
	protected void setPopUpShowDividend(Stage popUpShowDividend) {
		this.popUpShowDividend = popUpShowDividend;
	}

	/**
	 * @return the showDividend
	 */
	protected AnchorPane getShowDividend() {
		return showDividend;
	}

	/**
	 * @return the showDividendController
	 */
	protected ShowDividendController getShowDividendController() {
		return showDividendController;
	}

	/**
	 * @return the bundle
	 */
	protected ResourceBundle getBundle() {
		return bundle;
	}

	/**
	 * @return the detailStockMService
	 */
	protected IDetailStockMService getDetailStockMService() {
		return detailStockMService;
	}

	/**
	 * @return the loader
	 */
	protected FXMLLoader getLoader() {
		return loader;
	}

	/**
	 * @return the frontManager
	 */
	protected IFrontManager getFrontManager() {
		return frontManager;
	}

	/**
	 * @param showDividend
	 *            the showDividend to set
	 */
	protected void setShowDividend(AnchorPane showDividend) {
		this.showDividend = showDividend;
	}

	/**
	 * @param showDividendController
	 *            the showDividendController to set
	 */
	protected void setShowDividendController(
			ShowDividendController showDividendController) {
		this.showDividendController = showDividendController;
	}

}
