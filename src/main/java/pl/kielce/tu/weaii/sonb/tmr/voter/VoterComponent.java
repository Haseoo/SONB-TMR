package pl.kielce.tu.weaii.sonb.tmr.voter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.kielce.tu.weaii.sonb.tmr.common.JavalinServer;

import java.io.IOException;

import static pl.kielce.tu.weaii.sonb.tmr.common.Utils.getResourceURL;

public class VoterComponent extends Application {

    private JavalinServer server = new JavalinServer();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        var mainWindow = new FXMLLoader(getResourceURL("FXML/voter.fxml"));
        mainWindow.setController(new VoterController(server));
        Parent root = mainWindow.load();
        Scene scene = new Scene(root);
        stage.setTitle("Voter");
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
