package pl.kielce.tu.weaii.sonb.tmr.voter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.cxf.jaxrs.client.WebClient;
import pl.kielce.tu.weaii.sonb.tmr.common.ClientBuilder;
import pl.kielce.tu.weaii.sonb.tmr.common.JavalinServer;

import java.io.IOException;
import java.util.Arrays;

import static pl.kielce.tu.weaii.sonb.tmr.common.Utils.getResourceURL;

public class VoterComponent extends Application {

    private static final String CLIENT_IP = "localhost";

    private JavalinServer server = new JavalinServer();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        var mainWindow = new FXMLLoader(getResourceURL("FXML/voter.fxml"));
        var controller = new VoterController(server, new WebClient[]{
                new ClientBuilder().host(CLIENT_IP).timeout(100).port(9000).build(),
                new ClientBuilder().host(CLIENT_IP).timeout(100).port(9001).build(),
                new ClientBuilder().host(CLIENT_IP).timeout(100).port(9002).build()
        },
                Arrays.asList(8000, 8001, 8002),
                8000);
        mainWindow.setController(controller);
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
