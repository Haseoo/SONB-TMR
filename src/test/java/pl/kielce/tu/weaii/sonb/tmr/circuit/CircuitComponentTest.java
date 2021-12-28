package pl.kielce.tu.weaii.sonb.tmr.circuit;

import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.cxf.jaxrs.client.WebClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.TextMatchers;
import pl.kielce.tu.weaii.sonb.tmr.common.ClientBuilder;
import pl.kielce.tu.weaii.sonb.tmr.common.JavalinServer;
import pl.kielce.tu.weaii.sonb.tmr.common.dto.BitResponse;
import pl.kielce.tu.weaii.sonb.tmr.common.dto.Polynomial;

import java.io.IOException;
import java.util.Arrays;

import static pl.kielce.tu.weaii.sonb.tmr.common.Utils.getResourceURL;

class CircuitComponentTest extends ApplicationTest {
    protected static JavalinServer server;

    private final int componentTestedPort = 9000;
    private final WebClient circuitClient = new ClientBuilder().host("localhost").port(componentTestedPort).timeout(100000).build();

    private final int[] expectedBits = new int[]{0,0,1,0,0,0,1,0};

    @BeforeAll
    static void initServer(){server = new JavalinServer();}

    @AfterAll
    static void destroyServer(){ server.stop(); }

    Pane mainRoot;
    Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
        var mainWindow = new FXMLLoader(getResourceURL("FXML/circuit.fxml"));
        mainWindow.setController(getCircuitController());
        mainRoot = mainWindow.load();
        mainStage = stage;
        Scene scene = new Scene(mainRoot);
        stage.setTitle("Circuit");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        clickOn("#startBtn");
    }

    private BitResponse getOk(int val) {
        return new BitResponse(BitResponse.Status.OK, "OK", val);
    }

    @Test
    public void test_circuit(){
        should_display_expectedBits_after_falsification();
        should_get_bit();
        should_display_error_message();
        should_display_null_bits();
    }

    private void should_display_error_message() {
        //given

        //when
        makeRequest(new Polynomial(Arrays.asList(-4761,0,0)));

        //then
        FxAssert.verifyThat("#status",TextMatchers.hasText("No real root"));
    }

    private void should_display_null_bits(){
        //given

        //when
        makeRequest(1,null, BitResponse.Status.ERROR);

        //then
    }

    private void should_get_bit() {
        //given

        //when
        for(int i = 0; i < expectedBits.length; i++){
            makeRequest(i,expectedBits[i], BitResponse.Status.OK);
        }
        //then
    }

    private void should_display_expectedBits_after_falsification() {
        //given

        //when
        makeRequest(new Polynomial(Arrays.asList(-4761,0,1)));
        clickOn("#bit0");

        //then
        for(int i = 0; i < expectedBits.length; i++){
            FxAssert.verifyThat(String.format("#bit%d",i), TextMatchers.hasText(Integer.toString(expectedBits[i])));
        }
    }

    private CircuitController getCircuitController(){
        return new CircuitController(server);
    }

    private void makeRequest(Polynomial polynomial) {
        new Thread(() -> {
            circuitClient.replacePath("/equation").post(polynomial);
        }).start();
        sleep(2000);
    }

    private void makeRequest(final int bitNo,
                             final Integer expectedBit,
                             final BitResponse.Status expectedStatus) {
        new Thread(() -> {
            var response = circuitClient.replacePath("/bit").replaceQueryParam("no", bitNo).get(BitResponse.class);
            Assertions.assertThat(response).extracting(BitResponse::getStatus).isEqualTo(expectedStatus);
            Assertions.assertThat(response).extracting(BitResponse::getBitValue).isEqualTo(expectedBit);
        }).start();
        sleep(500);
    }
}
