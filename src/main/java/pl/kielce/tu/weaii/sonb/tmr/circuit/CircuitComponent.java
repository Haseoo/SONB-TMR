package pl.kielce.tu.weaii.sonb.tmr.circuit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pl.kielce.tu.weaii.sonb.tmr.main.MainController;

import java.io.IOException;

import static pl.kielce.tu.weaii.sonb.tmr.Utils.getResourceURL;

public class CircuitComponent extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        var mainWindow = new FXMLLoader(getResourceURL("FXML/circuit.fxml"));
        mainWindow.setController(new CircuitController());
        Parent root = mainWindow.load();
        Scene scene = new Scene(root);
        stage.setTitle("Circuit");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }


}
