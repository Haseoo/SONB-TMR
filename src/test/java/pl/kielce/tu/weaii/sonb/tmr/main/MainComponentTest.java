package pl.kielce.tu.weaii.sonb.tmr.main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.cxf.jaxrs.client.WebClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.TextInputControlMatchers;
import org.testfx.matcher.control.TextMatchers;
import pl.kielce.tu.weaii.sonb.tmr.common.dto.BitResponse;
import pl.kielce.tu.weaii.sonb.tmr.common.dto.Polynomial;

import java.io.IOException;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static pl.kielce.tu.weaii.sonb.tmr.common.Utils.getResourceURL;


class MainComponentTest extends ApplicationTest {

    Pane mainRoot;
    Stage mainStage;

    MainController mainController;

    WebClient circuitWebClientMock = Mockito.mock(WebClient.class);
    WebClient mainVoterWebClientMock = Mockito.mock(WebClient.class);

    @Override
    public void start(Stage stage) throws IOException {
        mainController = new MainController(new WebClient[]{
                circuitWebClientMock,
                circuitWebClientMock,
                circuitWebClientMock
        },
        mainVoterWebClientMock);
        var mainWindow = new FXMLLoader(getResourceURL("FXML/main.fxml"));
        mainWindow.setController(mainController);
        mainRoot = mainWindow.load();
        mainStage = stage;
        Scene scene = new Scene(mainRoot);
        stage.setTitle("Main");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @Test
    public void test_main_component() {
        when(circuitWebClientMock.replacePath(anyString())).thenReturn(circuitWebClientMock);
        final var expectedPolynomial = new Polynomial(Arrays.asList(-4761, 0, 1));

        should_enter_Polynomial();

        should_Send_Polynomial_to_circuits(expectedPolynomial);

        should_display_recived_bits();

        should_reset();
    }

    private void should_reset() {
        clickOn("#startBtn");
        Assertions.assertThat(mainRoot.lookup("#startBtn")).matches(element -> ((Button) element).getText().equals("Start"));
        for (int i = 0; i < 8; i++) {
            FxAssert.verifyThat("#bit" + i, TextMatchers.hasText(""));
        }
    }

    private void should_display_recived_bits() {
        final var bits = createBitList();
        when(mainVoterWebClientMock.replacePath(any())).thenReturn(mainVoterWebClientMock);
        when(mainVoterWebClientMock.replaceQueryParam(anyString(), any())).thenReturn(mainVoterWebClientMock);
        when(mainVoterWebClientMock.get(BitResponse.class)).thenReturn(bits[0], bits);

        mainVoterWebClientMock.get(BitResponse.class);

        for (int i = 0; i < 8; i++) {
            final var bitResponse = bits[i];
            clickOn("#startBtn");
            verify(mainVoterWebClientMock).replaceQueryParam("no", i);
            FxAssert.verifyThat("#bit" + i, TextMatchers.hasText(bitResponse.getBitValue().toString()));
        }
        verify(mainVoterWebClientMock, times(8)).replacePath("/bit");

        Assertions.assertThat(mainRoot.lookup("#startBtn")).matches(element -> ((Button) element).getText().equals("Reset"));
    }

    private void should_Send_Polynomial_to_circuits(Polynomial expectedPolynomial) {
        clickOn("#startBtn");
        Assertions.assertThat(mainRoot.lookup("#startBtn")).matches(element -> ((Button) element).getText().equals("Next bit"));

        verify(circuitWebClientMock, times(3)).post(eq(expectedPolynomial));
        verify(circuitWebClientMock, times(3)).replacePath("/equation");
    }

    private void should_enter_Polynomial() {
        clickOn("#polyn");
        write("2");
        clickOn("OK");
        write("1");
        clickOn("OK");
        write("0");
        clickOn("OK");
        write("0");
        clickOn("OK");
        write("-4761");
        clickOn("OK");
        write("-4761");
        clickOn("OK");

        FxAssert.verifyThat("#equationInput", TextInputControlMatchers.hasText("x^2-4761"));
    }

    private BitResponse[] createBitList() {
        return new BitResponse[]{
                getOk(1),
                getOk(0),
                getOk(1),
                getOk(0),
                getOk(0),
                getOk(0),
                getOk(1),
                getOk(0)};
    }

    private BitResponse getOk(int val) {
        return new BitResponse(BitResponse.Status.OK, "OK", val);
    }


    //wystartowac server
    // wysylam requesta /equalion , DAJE SLEEPA!!!
    //sprawdzam czy sie dobrze rozwiazaly bity w gornej linijce
    //sprawdz klikniecie na bit czy sie przekreca x2 ten sam
    //wywoule 8 razy endpoitna /bit daje sleepa
    //sprawdzam czy sie na dole pojawil sleep
    //PAMIETAC O SERVER STOP @AfterAll
    //znalesc webclienta
    //w circuit wywoluje endpoint klientem i oczekujesz ze sie pozmienia
    //

}
