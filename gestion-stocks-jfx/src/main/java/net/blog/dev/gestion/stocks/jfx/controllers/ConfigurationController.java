/**
 * 
 */
package net.blog.dev.gestion.stocks.jfx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import net.blog.dev.gestion.stocks.jfx.IFrontManager;
import net.blog.dev.gestion.stocks.middle.api.IConfigurationMSservice;
import net.blog.dev.gestion.stocks.middle.beans.ConfigurationBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Kiva
 * 
 */
public class ConfigurationController extends AnchorPane implements
		Initializable {

    static final Logger logger = LoggerFactory.getLogger(ConfigurationController.class);

	@FXML
	private Tooltip tooltipDirectory;

	@FXML
	private TextField srdLoanField;

	private ConfigurationBean configurationBean = new ConfigurationBean();

	@Inject
	private IConfigurationMSservice configurationMSservice;

	@Inject
	private IFrontManager frontManager;

	public void openFileChoose(ActionEvent event) {
		final ConfigurationBean configuration = configurationMSservice
				.getConfiguration();
		final DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setInitialDirectory(new File(configuration
				.getDirectory()));
		final File directory = directoryChooser.showDialog(frontManager
				.getWindowParent());
		if (directory != null) {
			configurationBean.setDirectory(directory.getAbsolutePath());
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
        logger.info("Initialize {} {}", arg0, arg1);
		configurationBean = configurationMSservice.getConfiguration();
		tooltipDirectory.setText(configurationBean.getDirectory());
		srdLoanField.setText(configurationBean.getSrdLoan().toString());
	}

	public void save(ActionEvent event) {
		configurationBean.setSrdLoan(Float.parseFloat(srdLoanField.getText()));
		configurationMSservice.setConfiguration(configurationBean);
	}

}
