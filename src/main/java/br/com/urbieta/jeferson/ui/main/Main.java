package br.com.urbieta.jeferson.ui.main;

import br.com.urbieta.jeferson.util.ApplicationUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Main extends Application {

    private static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        configureProject();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setTitle("RCA Login");
        ApplicationUtils.setStageIcon(stage);
        ApplicationUtils.setStageCloseEvent(stage);
        logger.info("Aplicação iniciada");
    }

    private void configureProject() {
        BasicConfigurator.configure();
        PropertyConfigurator.configure("src/main/resources/log4j.properties");
    }
}
