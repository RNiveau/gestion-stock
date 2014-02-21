package net.blog.dev.gestion.stocks.jfx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.VBox;
import net.blog.dev.gestion.stocks.middle.api.ICompareStockMService;
import net.blog.dev.gestion.stocks.middle.beans.CompareStockBean;
import net.blog.dev.gestion.stocks.middle.beans.CompareStockResultBean;
import net.blog.dev.gestion.stocks.middle.beans.CompareStockValueBean;

import javax.inject.Inject;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

public class CompareStockController implements Initializable {

	@FXML
	private VBox graphVbox;

	private ResourceBundle bundle;

	@Inject
	private ICompareStockMService compareStockMService;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		bundle = arg1;
		CompareStockBean compareStockBean = new CompareStockBean();
		compareStockBean.setDuration(3);
		compareStockBean.addValue("ALU");
		compareStockBean.addValue("GLE");
		final List<CompareStockResultBean> listCompareStock = compareStockMService
				.getListCompareStock(compareStockBean);
		final CategoryAxis xAxis = new CategoryAxis();
		xAxis.setLabel("Temps");
		ObservableList<XYChart.Series<String, Float>> lineChartData = FXCollections
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
				final Data<String, Float> data = new XYChart.Data<String, Float>(
                        compareStockValueBean.getDate() != null ? dateFormat.format(compareStockValueBean.getDate()) : "",
						compareStockValueBean.getValue());
				observableList.add(data);
			}
			final Series<String, Float> series = new LineChart.Series<String, Float>(
					compareStockResultBean.getName(), observableList);
			lineChartData.add(series);
		}
		NumberAxis yAxis = new NumberAxis("Variation", lower, higher, 1);
		LineChart chart = new LineChart(xAxis, yAxis, lineChartData);
		graphVbox.getChildren().add(chart);
	}
}
