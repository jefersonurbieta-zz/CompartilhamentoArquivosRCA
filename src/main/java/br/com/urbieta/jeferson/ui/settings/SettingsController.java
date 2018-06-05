package br.com.urbieta.jeferson.ui.settings;

import br.com.urbieta.jeferson.data.Preferences;
import br.com.urbieta.jeferson.util.PreferencesUtils;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    private static final Logger logger = Logger.getLogger(SettingsController.class);

    @FXML
    private JFXTextField diretorioArquivos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initDefaultValues();
    }

    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        String diretorio = diretorioArquivos.getText();
        Preferences preferencesUtils = PreferencesUtils.getPreferences();
        preferencesUtils.setDiretorioArquivos(diretorio);
        PreferencesUtils.writePreferenceToFile(preferencesUtils);
        logger.info("Local de Arquivos alterado para: " + diretorio);
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        ((Stage) diretorioArquivos.getScene().getWindow()).close();
    }

    private void initDefaultValues() {
        Preferences preferences = PreferencesUtils.getPreferences();
        diretorioArquivos.setText(String.valueOf(preferences.getDiretorioArquivos()));
    }

}
