/**
 * 
 */
package net.blog.dev.gestion.stocks.jfx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import net.blog.dev.gestion.stocks.middle.api.IDividendMService;
import net.blog.dev.gestion.stocks.middle.beans.DetailStockBean;
import net.blog.dev.gestion.stocks.middle.beans.DividendBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Kiva
 * 
 */
public class ShowDividendController {

    static final Logger logger = LoggerFactory.getLogger(ShowDividendController.class);

	@FXML
	private TableView<DividendBean> table;

	@Inject
	private IDividendMService dividendMService;

	public void close(ActionEvent event) {
		table.getScene().getWindow().hide();
	}

	public void init(DetailStockBean detailStockBean) {
        logger.info("init {}", detailStockBean);
		final List<DividendBean> dividends = dividendMService
				.getDividends(detailStockBean.getId());

		ObservableList<DividendBean> list = FXCollections.observableArrayList();
		list.addAll(dividends);
		table.setItems(list);

	}
}
