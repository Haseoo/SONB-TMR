package pl.kielce.tu.weaii.sonb.tmr.circuit;

import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import pl.kielce.tu.weaii.sonb.tmr.common.JavalinServer;
import org.mockito.Mockito;
import pl.kielce.tu.weaii.sonb.tmr.common.dto.BitResponse;
import pl.kielce.tu.weaii.sonb.tmr.common.dto.Polynomial;


import java.io.IOException;
import java.util.Arrays;

import static pl.kielce.tu.weaii.sonb.tmr.common.Constants.SERVER_STARTED;
import static pl.kielce.tu.weaii.sonb.tmr.common.Utils.getResourceURL;
import static org.mockito.Mockito.*;

class CircuitComponentTest extends ApplicationTest {


    private final JavalinServer javalinServer = new JavalinServer();

    Pane mainRoot;
    Stage mainStage;

    CircuitController circuitController;

    @Override
    public void start(Stage stage) throws IOException {
        circuitController = new CircuitController(javalinServer);
        var mainWindow = new FXMLLoader(getResourceURL("FXML/circuit.fxml"));
        mainWindow.setController(circuitController);
        mainRoot = mainWindow.load();
        mainStage = stage;
        Scene scene = new Scene(mainRoot);
        stage.setTitle("Circuit");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private BitResponse getOk(int val) {
        return new BitResponse(BitResponse.Status.OK, "OK", val);
    }

    @Test
    public void test_circuit(){
        //wystartowac server
        javalinServer.createAndStart(9000);

        Polynomial polynomial = new Polynomial();
        polynomial.setCoefficients(Arrays.asList(-4761,0,1));
        Integer root = polynomial.bisection(0,255);
        BitResponse[] expected = new BitResponse[]{
                getOk(1),
                getOk(0),
                getOk(1),
                getOk(0),
                getOk(0),
                getOk(0),
                getOk(1),
                getOk(0)};


        // wysylam requesta /equalion , DAJE SLEEPA!!!
        //sprawdzam czy sie dobrze rozwiazaly bity w gornej linijce
        //sprawdz klikniecie na bit czy sie przekreca x2 ten sam
        //wywoule 8 razy endpoitna /bit daje sleepa
        //sprawdzam czy sie na dole pojawil sleep
        //PAMIETAC O SERVER STOP @AfterAll
        //znalesc webclienta
        //w circuit wywoluje endpoint klientem i oczekujesz ze sie pozmienia


    }


}
