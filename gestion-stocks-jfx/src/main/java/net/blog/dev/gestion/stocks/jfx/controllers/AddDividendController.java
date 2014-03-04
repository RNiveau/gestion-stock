/**
 * 
 */
package net.blog.dev.gestion.stocks.jfx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import net.blog.dev.gestion.stocks.middle.Utils;
import net.blog.dev.gestion.stocks.middle.api.IDividendMService;
import net.blog.dev.gestion.stocks.middle.beans.AddDividendBean;
import net.blog.dev.gestion.stocks.middle.beans.DetailStockBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Date;
import java.util.Map;

/**
 * @author Kiva
 * 
 */
public class AddDividendController {

    static final Logger logger = LoggerFactory.getLogger(AddDividendController.class);

    private DetailStockBean detailStockBean;

	@Inject
	private IDividendMService dividendMService;

	@FXML
	private TextField date;

	@FXML
	private TextField price;

	public void add(ActionEvent event) {
		final AddDividendBean addDidivdendBean = new AddDividendBean();
		addDidivdendBean.setDate(date.getText());
		addDidivdendBean.setPrice(price.getText());
		addDidivdendBean.setId(detailStockBean.getId());
		final Map<String, String> mapError = dividendMService
				.validate(addDidivdendBean);
		if (!mapError.isEmpty()) {

		} else {
			dividendMService.addDividend(addDidivdendBean);
		}
		date.getScene().getWindow().hide();
	}

	public void init(DetailStockBean detailStockBean) {
        logger.info("init {}", detailStockBean);
		this.detailStockBean = detailStockBean;
		date.setText(Utils.formatDate(new Date(), "dd/MM/yyyy"));
	}

}
