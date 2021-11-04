package pl.kielce.tu.weaii.sonb.tmr.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

import static pl.kielce.tu.weaii.sonb.tmr.Utils.getResourceURL;

public class MainComponent extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        var mainWindow = new FXMLLoader(getResourceURL("FXML/main.fxml"));
        mainWindow.setController(new MainController());
        Parent root = mainWindow.load();
        Scene scene = new Scene(root);
        stage.setTitle("Main");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

}
