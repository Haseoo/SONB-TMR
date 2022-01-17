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
import java.util.Collections;

import static pl.kielce.tu.weaii.sonb.tmr.common.Utils.getResourceURL;

public class MainVoterComponent extends Application {

    private JavalinServer server = new JavalinServer();

    public static void main(String[] args) {
        launch(args);
    }

    private static final String VOTER_IP = "localhost";

    @Override
    public void start(Stage stage) throws IOException {
        var mainWindow = new FXMLLoader(getResourceURL("FXML/voter.fxml"));
        var controller = new VoterController(server,
                new WebClient[]{
                        new ClientBuilder().host(VOTER_IP).port(8000).timeout(3000).build(),
                        new ClientBuilder().host(VOTER_IP).port(8001).timeout(3000).build(),
                        new ClientBuilder().host(VOTER_IP).port(8002).timeout(3000).build()
                },
                Collections.singletonList(7000),
                7000);
        mainWindow.setController(controller);
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
