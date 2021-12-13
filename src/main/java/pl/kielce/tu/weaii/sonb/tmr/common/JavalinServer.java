package pl.kielce.tu.weaii.sonb.tmr.common;

import io.javalin.Javalin;
import io.javalin.http.Handler;
import javafx.scene.control.Alert;


public class JavalinServer {

    private Javalin javalin;

    public void createAndStart(int port) {
        try {
            this.javalin = Javalin.create().start(port);
        } catch (Exception e) {
            var alert = new Alert(Alert.AlertType.ERROR, "A critical error has occurred");
            alert.setHeaderText(e.getLocalizedMessage());
            alert.showAndWait();
        }
    }

    public void stop() {
        if (this.javalin != null) {
            this.javalin.stop();
        }
    }

    public void addGetEndpoint(String endpoint, Handler handler) {
        javalin.get(endpoint, handler);
    }

    public void addPostEndpoint(String endpoint, Handler handler) {
        javalin.post(endpoint, handler);
    }

}
