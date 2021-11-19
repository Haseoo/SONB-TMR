package pl.kielce.tu.weaii.sonb.tmr.common;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.apache.cxf.jaxrs.client.WebClient;

import javax.ws.rs.core.MediaType;
import java.util.Collections;

public class ClientBuilder {
    private String host;
    private Integer port;


    public ClientBuilder host(String host) {
        this.host = host;
        return this;
    }

    public ClientBuilder port(int port) {
        this.port = port;
        return this;
    }

    public WebClient build() {
        if (host == null || host.equals("")) {
            throw new IllegalArgumentException("Provide host");
        }
        if (port == null) {
            throw new IllegalArgumentException("Provide port");
        }

        var client = WebClient.create(String.format("http://%s:%s", host, port),
                Collections.singletonList(new JacksonJaxbJsonProvider()));
        client = client.accept(MediaType.APPLICATION_JSON_TYPE);
        client = client.type(MediaType.APPLICATION_JSON_TYPE);
        var conduit = WebClient.getConfig(client).getHttpConduit();
        conduit.getClient().setConnectionTimeout(3000);
        conduit.getClient().setReceiveTimeout(3000);

        return client;
    }

}
