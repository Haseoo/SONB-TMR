package pl.kielce.tu.weaii.sonb.tmr.mainvoter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.kielce.tu.weaii.sonb.tmr.common.JavalinServer;

import java.io.IOException;

import static pl.kielce.tu.weaii.sonb.tmr.common.Utils.getResourceURL;

public class MainVoterComponent extends Application {

    private JavalinServer server = new JavalinServer();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        var mainWindow = new FXMLLoader(getResourceURL("FXML/voter.fxml"));
        mainWindow.setController(new MainVoterController(server));
        Parent root = mainWindow.load();
        Scene scene = new Scene(root);
        stage.setTitle("Main voter");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        server.stop();
    }
}
