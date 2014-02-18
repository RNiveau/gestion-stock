package net.blog.dev.gestion.stocks.jfx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import net.blog.dev.gestion.stocks.middle.beans.StockListBean;

public abstract class AbstractStocksListController implements Initializable {

	@FXML
	private TableView<StockListBean> stocksList;

	/**
	 * @return the stocksList
	 */
	public TableView<StockListBean> getStocksList() {
		return stocksList;
	}

}