package pl.kielce.tu.weaii.sonb.tmr.common;

import io.javalin.Javalin;
import io.javalin.http.Handler;


public class JavalinServer {

    private Javalin javalin;

    public void createAndStart(int port) {
        this.javalin = Javalin.create().start(port);
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
