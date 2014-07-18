/**
 *
 */
package net.blog.dev.gestion.stocks.jfx.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;
import net.blog.dev.gestion.stocks.jfx.FxmlFileConstantes;
import net.blog.dev.gestion.stocks.jfx.IFrontManager;
import net.blog.dev.gestion.stocks.jfx.JfxUtils;
import net.blog.dev.gestion.stocks.middle.beans.StockListBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

import static net.blog.dev.gestion.stocks.jfx.FrontUtils.formatDate;
import static net.blog.dev.gestion.stocks.jfx.FrontUtils.formatStringToDate;

/**
 * @author Kiva
 */
public class StockListTableController extends VBox implements Initializable {

    static final Logger logger = LoggerFactory.getLogger(StockListTableController.class);

    @FXML
    private TableView<StockListBean> stocksList;

    @FXML
    private TableColumn<StockListBean, Integer> quantityColumn;

    @FXML
    private TextField filter;

    @FXML
    private TableColumn<StockListBean, String> dateColumn;

    private Tooltip tooltip;

    private IDetailController detailStockController;

    private Pane detail;

    @Inject
    private FXMLLoader loader;

    @Inject
    private IFrontManager frontManager;

    ObservableList<StockListBean> initialItems;

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
            if (detailStockController != null) {
                detailStockController.loadFromStockListBean(stocksList
                        .getSelectionModel().getSelectedItem());
                setTooltipContent(detail);
            }
            try {
                Thread.currentThread().sleep(100l);
            } catch (InterruptedException e) {
            }
            tooltip.show(stocksList, event.getScreenX(), event.getScreenY());
        }
    }

    public void setTooltipContent(Node content) {
        final Pane root = (Pane) tooltip.getScene().getRoot();
        root.getChildren().clear();
        root.getChildren().add(content);
    }

    /**
     * @param detailStockController the detailStockController to set
     */
    public void setDetailStockController(IDetailController detailStockController) {
        this.detailStockController = detailStockController;
    }

    /**
     * @param detail the detail to set
     */
    public void setDetail(Pane detail) {
        this.detail = detail;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        logger.info("Initialize {} {}", arg0, arg1);
        dateColumn.setComparator((o1, o2) -> formatStringToDate(o1, "dd/MM/yyyy").compareTo(formatStringToDate(o2, "dd/MM/yyyy")));
        dateColumn.setCellValueFactory(value -> new SimpleStringProperty(formatDate(value.getValue().getDate(), "dd/MM/yyyy")));
    }

    /**
     * @return the quantityColumn
     */
    public TableColumn<StockListBean, Integer> getQuantityColumn() {
        return quantityColumn;
    }

    protected TextField getFilter() {
        return filter;
    }

    public void filter(KeyEvent event) {
        if (initialItems == null)
            initialItems = stocksList.getItems();
        ObservableList<StockListBean> items = FXCollections.observableArrayList();
        if (CollectionUtils.isNotEmpty(initialItems)) {
            for (StockListBean initialItem : initialItems) {
                if (StringUtils.containsIgnoreCase(initialItem.getTitle(), filter.getText()) || StringUtils.containsIgnoreCase(initialItem.getCode(), filter.getText()))
                    items.add(initialItem);
            }
        }
        stocksList.setItems(items);
    }

}
