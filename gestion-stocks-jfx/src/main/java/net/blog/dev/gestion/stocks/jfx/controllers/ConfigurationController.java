/**
 * 
 */
package net.blog.dev.gestion.stocks.jfx.controllers;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.blog.dev.gestion.stocks.jfx.IFrontManager;
import net.blog.dev.gestion.stocks.middle.api.IConfigurationMService;
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

    @FXML
    private TextField idDropboxField;

    private ConfigurationBean configurationBean = new ConfigurationBean();

	@Inject
	private IConfigurationMService configurationMSservice;

	@Inject
	private IFrontManager frontManager;

    @FXML
    private AnchorPane dropbox;


    private Stage popUpDropbox;

    @FXML
    private DropboxController dropboxController;

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
        init();
	}

    private void init() {
        configurationBean = configurationMSservice.getConfiguration();
        tooltipDirectory.setText(configurationBean.getDirectory());
        srdLoanField.setText(configurationBean.getSrdLoan().toString());
        idDropboxField.setText(configurationBean.getIdDropbox());
        idDropboxField.setEditable(false);
    }

    public void save(ActionEvent event) {
		configurationBean.setSrdLoan(Float.parseFloat(srdLoanField.getText()));
		configurationMSservice.setConfiguration(configurationBean);
	}

    public void openDropbox(ActionEvent actionEvent) {
        if (popUpDropbox == null) {
            popUpDropbox = new Stage();
            popUpDropbox.initModality(Modality.WINDOW_MODAL);
            popUpDropbox.initOwner(frontManager.getWindowParent());
            popUpDropbox.setScene(new Scene(new Group(dropbox)));
        }
        popUpDropbox.show();
        popUpDropbox.addEventHandler(
                CloseController.CLOSE_WINDOW_CLOSE_STOCK, event -> init());
        dropboxController.init();
    }
}
