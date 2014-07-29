package net.blog.dev.gestion.stocks.jfx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.blog.dev.gestion.stocks.middle.api.IConfigurationMService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Created by Xebia on 29/07/2014.
 */
public class DropboxController {

    static final Logger logger = LoggerFactory.getLogger(AddDividendController.class);

    @FXML
    private TextField urlField;

    @FXML
    private TextField codeField;

    @Inject
    private IConfigurationMService configurationMService;

    public void authorize(ActionEvent event) {
        if (StringUtils.isNotBlank(codeField.getText())) {
            configurationMService.saveDropboxAccount(codeField.getText());
            codeField.getScene()
                    .getWindow()
                    .fireEvent(
                            new WindowEvent(codeField.getScene().getWindow(),
                                    CloseController.CLOSE_WINDOW_CLOSE_STOCK));
            ((Stage) codeField.getScene().getWindow()).close();
        }
    }

    public void init() {
        urlField.setText(configurationMService.getDropboxUrl());
        urlField.setEditable(false);
    }
}
