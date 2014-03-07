package net.blog.dev.gestion.stocks.jfx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.blog.dev.gestion.stocks.middle.api.ICompareStockMService;
import net.blog.dev.gestion.stocks.middle.beans.CompareStockBean;
import net.blog.dev.gestion.stocks.middle.beans.CompareStockResultBean;
import net.blog.dev.gestion.stocks.middle.beans.CompareStockValueBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CompareStockController implements Initializable {

    static final Logger logger = LoggerFactory.getLogger(CompareStockController.class);

    @FXML
    private VBox graphVbox;

    @FXML
    private Button runButton;

    @FXML
    private VBox containerText;

    @FXML
    private ChoiceBox<String> duration;

    private ResourceBundle bundle;

    @Inject
    private ICompareStockMService compareStockMService;

    private List<HBox> listCodeHbox;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        logger.info("Initialize {} {}", arg0, arg1);
        bundle = arg1;
        listCodeHbox = new ArrayList<>();
        listCodeHbox.add(buildLineCode(false));
        listCodeHbox.add(buildLineCode(false));
        containerText.getChildren().add(listCodeHbox.get(0));
        containerText.getChildren().add(listCodeHbox.get(1));
    }

    private HBox buildLineCode(boolean addDeleteButton) {
        HBox hBox = new HBox();
        Label label = new Label();
        label.setText("Code action: ");
        hBox.getChildren().add(label);
        TextField textField = new TextField();
        textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                int textFill = 0;
                for (HBox box : listCodeHbox) {
                    if (StringUtils.isNotBlank(((TextField) box.getChildren().get(1)).getText()))
                        textFill++;
                }
                runButton.setDisable(!(textFill >= 2 && duration.getValue() != null));
            }
        });
        hBox.getChildren().add(textField);
        if (addDeleteButton) {
            Button button = new Button();
            button.setText("Supprimer code");
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Parent parent = ((Button) actionEvent.getSource()).getParent();
                    listCodeHbox.remove(parent);
                    containerText.getChildren().remove(parent);
                }
            });
            hBox.getChildren().add(button);
        }
        return hBox;
    }

    public void addCode(ActionEvent event) {
        HBox hbox = buildLineCode(true);
        listCodeHbox.add(hbox);
        containerText.getChildren().add(hbox);
    }

    public void runCompare() {
        CompareStockBean compareStockBean = new CompareStockBean();
        compareStockBean.setDuration(Integer.parseInt(duration.getValue().toString()));
        for (HBox hBox : listCodeHbox) {
            String text = ((TextField) hBox.getChildren().get(1)).getText();
            if (StringUtils.isNotBlank(text))
                compareStockBean.addValue(text);
        }
        final List<CompareStockResultBean> listCompareStock = compareStockMService
                .getListCompareStock(compareStockBean);
        final CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Temps");
        ObservableList<Series<String, Float>> lineChartData = FXCollections
                .observableArrayList();
        Float higher = 0f;
        Float lower = 0f;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        for (CompareStockResultBean compareStockResultBean : listCompareStock) {
            final ObservableList<Data<String, Float>> observableList = FXCollections
                    .observableArrayList();
            if (compareStockResultBean.getHigher() > higher)
                higher = compareStockResultBean.getHigher();
            if (compareStockResultBean.getLower() < lower)
                lower = compareStockResultBean.getLower();
            for (CompareStockValueBean compareStockValueBean : compareStockResultBean
                    .getListValue()) {
                final Data<String, Float> data = new Data<String, Float>(
                        compareStockValueBean.getDate() != null ? dateFormat.format(compareStockValueBean.getDate()) : "",
                        compareStockValueBean.getValue());
                observableList.add(data);
            }
            final Series<String, Float> series = new Series<String, Float>(
                    compareStockResultBean.getName(), observableList);
            lineChartData.add(series);
        }
        NumberAxis yAxis = new NumberAxis("Variation", lower, higher, 1);
        LineChart chart = new LineChart(xAxis, yAxis, lineChartData);
        chart.setMinWidth(500);
        chart.setMinHeight(500);
        graphVbox.getChildren().clear();
        graphVbox.getChildren().add(chart);
    }
}
