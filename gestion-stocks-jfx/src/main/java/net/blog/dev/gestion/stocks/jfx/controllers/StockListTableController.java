/**
 * 
 */
package net.blog.dev.gestion.stocks.jfx.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;
import net.blog.dev.gestion.stocks.jfx.FxmlFileConstantes;
import net.blog.dev.gestion.stocks.jfx.IFrontManager;
import net.blog.dev.gestion.stocks.jfx.JfxUtils;
import net.blog.dev.gestion.stocks.middle.beans.StockListBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Kiva
 * 
 */
public class StockListTableController extends VBox implements Initializable {

    static final Logger logger = LoggerFactory.getLogger(StockListTableController.class);

	@FXML
	private TableView<StockListBean> stocksList;

	@FXML
	private TableColumn<StockListBean, Integer> quantityColumn;

	private Tooltip tooltip;

	private IDetailController detailStockController;

	private Pane detail;

	@Inject
	private FXMLLoader loader;

	@Inject
	private IFrontManager frontManager;

	/**
	 * @return the stocksList
	 */
	public TableView<StockListBean> getStocksList() {
		return stocksList;
	}

	public void openDetail(MouseEvent event) {
		if (event.getButton().equals(MouseButton.SECONDARY)) {
			if (tooltip == null) {
				tooltip = new Tooltip();
				tooltip.addEventHandler(
						CloseController.CLOSE_WINDOW_CLOSE_STOCK,
						new EventHandler<WindowEvent>() {

							@Override
							public void handle(WindowEvent event) {
								// chargement de la liste des positions fermees
								event.consume();
								final Node newRoot = JfxUtils.loadFxml(loader,
										FxmlFileConstantes.LIST_STOCKS_CLOSE);
								JfxUtils.loadInMainScene(stocksList.getScene(),
										(Parent) newRoot);
							}
						});
			}
			tooltip.show(stocksList, event.getScreenX(), event.getScreenY());
			if (detailStockController != null) {
				detailStockController.loadFromStockListBean(stocksList
						.getSelectionModel().getSelectedItem());
				setTooltipContent(detail);
			}
		}
	}

	public void setTooltipContent(Node content) {
		final Group root = (Group) tooltip.getScene().getRoot();
		root.getChildren().clear();
		root.getChildren().add(content);
	}

	/**
	 * @param detailStockController
	 *            the detailStockController to set
	 */
	public void setDetailStockController(IDetailController detailStockController) {
		this.detailStockController = detailStockController;
	}

	/**
	 * @param detail
	 *            the detail to set
	 */
	public void setDetail(Pane detail) {
		this.detail = detail;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
        logger.info("Initialize {} {}", arg0, arg1);
	}

	/**
	 * @return the quantityColumn
	 */
	public TableColumn<StockListBean, Integer> getQuantityColumn() {
		return quantityColumn;
	}
}
