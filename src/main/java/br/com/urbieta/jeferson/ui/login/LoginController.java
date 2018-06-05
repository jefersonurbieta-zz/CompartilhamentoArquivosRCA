package br.com.urbieta.jeferson.ui.login;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jfoenix.controls.JFXTextField;

import br.com.urbieta.jeferson.data.Preferences;
import br.com.urbieta.jeferson.ui.main.MainController;
import br.com.urbieta.jeferson.util.AlertUtils;
import br.com.urbieta.jeferson.util.ApplicationUtils;
import br.com.urbieta.jeferson.util.FileUtils;
import br.com.urbieta.jeferson.util.PreferencesUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.stage.DirectoryChooser;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginController implements Initializable {

	Preferences preference;

	private DirectoryChooser directoryChooser;

	@FXML
	private JFXTextField diretorioArquivos;

	@FXML
	private Button pesquisaBtn;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		preference = PreferencesUtils.getPreferences();
		if (ApplicationUtils.isNotNullOrBlank(preference.getDiretorioArquivos())) {
			diretorioArquivos.setText(preference.getDiretorioArquivos());
			if (!FileUtils.validarExistenciaDiretorio(preference.getDiretorioArquivos())) {
				AlertUtils.showErrorMessage("Diretorio não encontrado",
						"Preencha o campo com um caminho valido de um diretorio.");
			}
		}
		configuringDirectoryChooser();
		adicionarListener();
	}

	private void configuringDirectoryChooser() {
		directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Selecione um diretorio");
		directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
	}

	private void adicionarListener() {
		pesquisaBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				File dir = directoryChooser.showDialog(pesquisaBtn.getScene().getWindow());
				if (dir != null) {
					diretorioArquivos.setText(dir.getAbsolutePath());
				}
			}
		});
	}

	@FXML
	private void handleLoginButtonAction(ActionEvent event) {
		if (!FileUtils.validarExistenciaDiretorio(diretorioArquivos.getText())) {
			AlertUtils.showErrorMessage("Diretorio não encontrado",
					"Preencha o campo com um caminho valido de um diretorio.");
			diretorioArquivos.getStyleClass().add("wrong-credentials");
			return;
		}
		if (ApplicationUtils.isNotNullOrBlank(diretorioArquivos.getText())) {
			preference.setDiretorioArquivos(diretorioArquivos.getText());
			PreferencesUtils.writePreferenceToFile(preference);
			closeStage();
			loadMain();
		} else {
			if (ApplicationUtils.isNullOrBlank(diretorioArquivos.getText())) {
				diretorioArquivos.getStyleClass().add("wrong-credentials");
			}
		}
	}

	@FXML
	private void handleCancelButtonAction(ActionEvent event) {
		System.exit(0);
	}

	private void closeStage() {
		((Stage) diretorioArquivos.getScene().getWindow()).close();
	}

	void loadMain() {
		try {
			Parent parent = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle("RCA");
			stage.setScene(new Scene(parent));
			stage.show();
			ApplicationUtils.setStageIcon(stage);
			ApplicationUtils.setStageCloseEvent(stage);
		} catch (IOException ex) {
			Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
