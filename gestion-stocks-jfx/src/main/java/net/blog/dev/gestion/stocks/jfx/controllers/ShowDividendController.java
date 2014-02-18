/**
 * 
 */
package net.blog.dev.gestion.stocks.jfx.controllers;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import javax.inject.Inject;

import net.blog.dev.gestion.stocks.middle.api.IDividendMService;
import net.blog.dev.gestion.stocks.middle.beans.DetailStockBean;
import net.blog.dev.gestion.stocks.middle.beans.DividendBean;

/**
 * @author Kiva
 * 
 */
public class ShowDividendController {

	@FXML
	private TableView<DividendBean> table;

	@Inject
	private IDividendMService dividendMService;

	public void close(ActionEvent event) {
		table.getScene().getWindow().hide();
	}

	public void init(DetailStockBean detailStockBean) {
		final List<DividendBean> dividends = dividendMService
				.getDividends(detailStockBean.getId());

		ObservableList<DividendBean> list = FXCollections.observableArrayList();
		list.addAll(dividends);
		table.setItems(list);

	}
}
