package pl.kielce.tu.weaii.sonb.tmr;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.transport.ConduitInitiatorManager;
import org.apache.cxf.transport.http.HTTPTransportFactory;
import pl.kielce.tu.weaii.sonb.tmr.circuit.CircuitComponent;

public class Main {
    public static void main(String[] args) {
        initializeCxf();
        CircuitComponent.main(args);
    }

    private static void initializeCxf() {
        final Bus defaultBus = BusFactory.getDefaultBus();
        final ConduitInitiatorManager extension = defaultBus.getExtension(ConduitInitiatorManager.class);
        extension.registerConduitInitiator("http://cxf.apache.org/transports/http", new HTTPTransportFactory());
    }
}
