/**
 *
 */
package net.blog.dev.gestion.stocks.jfx.controllers;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import net.blog.dev.gestion.stocks.jfx.IFrontManager;
import net.blog.dev.gestion.stocks.jfx.JfxUtils;
import net.blog.dev.gestion.stocks.jfx.PoolThreadManager;
import net.blog.dev.gestion.stocks.middle.Utils;
import net.blog.dev.gestion.stocks.middle.api.IDetailStockMService;
import net.blog.dev.gestion.stocks.middle.api.IStocksListMService;
import net.blog.dev.gestion.stocks.middle.beans.StockListBean;
import net.blog.dev.gestion.stocks.middle.beans.StockListRunningBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;

/**
 * @author Kiva
 */
public class StocksListRunningController extends ScrollPane implements
        Initializable {

    static final Logger logger = LoggerFactory.getLogger(StocksListRunningController.class);

    @Inject
    private IStocksListMService stocksListMService;

    @Inject
    private IDetailStockMService detailStockMService;

    @Inject
    private IFrontManager frontManager;

    @FXML
    private TableColumn<StockListBean, Float> actualPriceColumn;

    @FXML
    private VBox tableListStock;

    @FXML
    private StockListTableController tableListStockController;

    @FXML
    private Pane popupDetailRunning;

    @FXML
    private AnchorPane scrollPane;

    @FXML
    private DetailStockRunningController popupDetailRunningController;

    private TableColumn<StockListBean, Number> columnActualPrice = new TableColumn<StockListBean, Number>();

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        logger.info("Initialize {} {}", arg0, arg1);
        scrollPane.setPrefWidth(frontManager.getWindowParent().getWidth()
                - JfxUtils.SPACE_LEFT_PANEL);

        final ObservableList<StockListBean> list = FXCollections
                .observableArrayList();
        final List<StockListBean> stocksListRunning = stocksListMService.getStocksListRunning();
        list.addAll(stocksListRunning);
        final TableColumn<StockListBean, Number> columnAtr = new TableColumn<StockListBean, Number>();
        setColumnAtr(columnAtr);
        setColumnQuantity();
        setColumnActualPrice();
        tableListStockController.getStocksList().setEditable(true);
        tableListStockController.getStocksList().getColumns().add(columnAtr);
        tableListStockController.getStocksList().setItems(list);
        tableListStockController.setDetail(popupDetailRunning);
        tableListStockController
                .setDetailStockController(popupDetailRunningController);

        final ExecutorService excecutor = PoolThreadManager.getPoolThread();
        for (final StockListBean stockListBean : stocksListRunning) {
           final Task<Float> task = new Task<Float>() {
                @Override
                protected Float call() throws Exception {
                    logger.debug("In jfx task for {}", stockListBean.getCode());
                    return stocksListMService.getActualPrice(stockListBean.getCode());
                }
            };
            task.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error(String.valueOf(task.getException()));
                }
            });
            task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.debug("Task succed, refresh screen");
                    ((StockListRunningBean) stockListBean).setActualPrice((Float) workerStateEvent.getSource().getValue());
                    columnActualPrice.setVisible(false);
                    columnActualPrice.setVisible(true);
                }
            });
            excecutor.execute(task);
        }
    }

    /**
     *
     */
    private void setColumnQuantity() {
        final TableColumn<StockListBean, Integer> quantityColumn = tableListStockController
                .getQuantityColumn();
        quantityColumn
                .setCellFactory(new Callback<TableColumn<StockListBean, Integer>, TableCell<StockListBean, Integer>>() {

                    @Override
                    public TableCell<StockListBean, Integer> call(
                            TableColumn<StockListBean, Integer> arg0) {
                        return new TextFieldTableCell<>(
                                new IntegerStringConverter());
                    }
                });
        quantityColumn
                .setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<StockListBean, Integer>>() {

            @Override
            public void handle(
                    CellEditEvent<StockListBean, Integer> arg0) {
                if (arg0.getNewValue() != null
                        && arg0.getNewValue() > 0) {
                    detailStockMService.updateQuantity(
                            arg0.getRowValue(), arg0.getNewValue());
                    arg0.getRowValue().setQuantity(arg0.getNewValue());
                    // force le raffraichissement du tableau
                    quantityColumn.setVisible(false);
                    quantityColumn.setVisible(true);
                }
            }
        });
    }

    private void setColumnActualPrice() {
        final PropertyValueFactory propertyUnitPrice = new PropertyValueFactory(
                "actualPrice");
        columnActualPrice.setCellValueFactory(propertyUnitPrice);
        columnActualPrice.setText("Prix actuel");
        columnActualPrice.setPrefWidth(100);
        tableListStockController.getStocksList().getColumns().add(columnActualPrice);
    }

    /**
     * @param columnAtr
     */
    private void setColumnAtr(final TableColumn<StockListBean, Number> columnAtr) {
        final Callback<TableColumn.CellDataFeatures<StockListBean, Number>, ObservableValue<Number>> callback = new Callback<TableColumn.CellDataFeatures<StockListBean, Number>, ObservableValue<Number>>() {

            @Override
            public ObservableNumberValue call(
                    CellDataFeatures<StockListBean, Number> arg0) {
                if (((StockListRunningBean) arg0.getValue()).getAtr() != null)
                    return new SimpleFloatProperty(
                            ((StockListRunningBean) arg0.getValue()).getAtr());
                else
                    return new SimpleFloatProperty(0f);
            }
        };
        columnAtr.setCellValueFactory(callback);
        columnAtr.setText("ATR");
        columnAtr.setPrefWidth(70d);
        Callback<TableColumn<StockListBean, Number>, TableCell<StockListBean, Number>> cellFactory = new Callback<TableColumn<StockListBean, Number>, TableCell<StockListBean, Number>>() {

            @SuppressWarnings("rawtypes")
            public TableCell call(TableColumn p) {
                final TextFieldTableCell<StockListBean, Number> textFieldTableCell = new TextFieldTableCell<>();
                textFieldTableCell.setConverter(new StringConverter<Number>() {

                    @Override
                    public String toString(Number arg0) {
                        return arg0 != null && arg0.floatValue() > 0f ? arg0
                                .toString() : "";
                    }

                    @Override
                    public Number fromString(String arg0) {
                        arg0 = arg0.replaceAll("[^0-9.,]+", "");
                        arg0 = arg0.replaceAll(",", ".");
                        return Utils.getFloat(arg0);
                    }
                });
                return textFieldTableCell;
            }
        };
        columnAtr.setCellFactory(cellFactory);
        columnAtr
                .setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<StockListBean, Number>>() {

                    @Override
                    public void handle(CellEditEvent<StockListBean, Number> arg0) {
                        final StockListBean stockListBean = arg0.getRowValue();
                        final Float atr = arg0.getNewValue() != null ? arg0
                                .getNewValue().floatValue() : null;
                        detailStockMService.updateAtr(stockListBean, atr);
                        ((StockListRunningBean) stockListBean).setAtr(atr);
                    }
                });
    }
}
