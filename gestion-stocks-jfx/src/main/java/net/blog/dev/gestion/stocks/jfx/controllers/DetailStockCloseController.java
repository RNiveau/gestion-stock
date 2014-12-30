/**
 * 
 */
package net.blog.dev.gestion.stocks.jfx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import net.blog.dev.gestion.stocks.middle.beans.StockListBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Kiva
 * 
 */
public class DetailStockCloseController extends AbstractDetailController {

    static final Logger logger = LoggerFactory.getLogger(DetailStockCloseController.class);

    @FXML
	private Label taxes;

	@FXML
	private Label taxesClose;

	@FXML
	private Label account;
	@FXML
	private Label direction;
	@FXML
	private Label type;
	@FXML
	private Label strategy;

    @FXML
    private Label dividends;

	@FXML
	private AnchorPane showDividend;

	@FXML
	private ShowDividendController showDividendController;

    @FXML
    private Label taxesPercentage;
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.blog.dev.gestion.stocks.jfx.controllers.IDetailController#
	 * loadFromStockListBean
	 * (net.blog.dev.gestion.stocks.middle.beans.StockListBean)
	 */
	@Override
	public void loadFromStockListBean(StockListBean bean) {
		setDetailStockBean(getDetailStockMService().getDetailStockBean(bean));
		taxes.setText(getDetailStockBean().getTaxes().toString());
		taxesClose.setText(getDetailStockBean().getTaxesClose().toString());
        taxesPercentage.setText(getDetailStockBean().getTaxesPercentage().toString());
		strategy.setText(getBundle().getString(
				getDetailStockBean().getStrategy()));
        dividends.setText(getDetailStockBean().getDividends().toString() + " %");
		account.setText(getBundle()
				.getString(getDetailStockBean().getAccount()));
		type.setText(getBundle().getString(getDetailStockBean().getType()));
		direction.setText(getBundle().getString(
				getDetailStockBean().getDirection()));
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
        logger.info("Initialize {} {}", arg0, arg1);
		setShowDividend(showDividend);
		setShowDividendController(showDividendController);
		super.initialize(arg0, arg1);
	}

}
