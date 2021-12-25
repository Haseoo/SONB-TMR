package pl.kielce.tu.weaii.sonb.tmr.voter;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.cxf.jaxrs.client.WebClient;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.TextMatchers;
import pl.kielce.tu.weaii.sonb.tmr.common.ClientBuilder;
import pl.kielce.tu.weaii.sonb.tmr.common.JavalinServer;
import pl.kielce.tu.weaii.sonb.tmr.common.dto.BitResponse;

import java.io.IOException;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static pl.kielce.tu.weaii.sonb.tmr.common.Utils.getResourceURL;


class VoterComponentsTest extends ApplicationTest {


    WebClient circuitWebClientMock = Mockito.mock(WebClient.class);

    private final int componentTestedPort = 8000;
    final WebClient voterClient = new ClientBuilder().host("localhost").port(componentTestedPort).timeout(10000).build();
    protected static JavalinServer server;


    @BeforeAll
    static void initServer() {
        server = new JavalinServer();
    }

    @AfterAll
    static void destroyServer() {
        server.stop();
    }

    @Test
    void voter_component_tests() {
        should_display_0_when_three_0s_from_circuits();
        should_display_0_when_two_0s_and_one_1_from_circuits();
        should_display_1_when_one_0_and_two_1s_from_circuits();
        should_display_1_three_1s_from_circuits();
        should_display_x_three_x_from_circuits();
    }

    void should_display_0_when_three_0s_from_circuits() {
        //given
        final var bitNo = 0;
        mockWebClientBuildRequestMethod(bitNo);
        when(circuitWebClientMock.get(BitResponse.class)).thenReturn(getOk(0));
        //when
        makeRequest(bitNo, 0, BitResponse.Status.OK);
        //then
        verifyBits("0", "0", "0", "0");
    }

    void should_display_0_when_two_0s_and_one_1_from_circuits() {
        //given
        final var bitNo = 1;
        mockWebClientBuildRequestMethod(bitNo);
        when(circuitWebClientMock.get(BitResponse.class)).thenReturn(getOk(0))
                .thenReturn(getOk(0))
                .thenReturn(getOk(1));
        //when
        makeRequest(bitNo, 0, BitResponse.Status.OK);
        //then
        verifyBits("0", "0", "1", "0");
    }

    void should_display_1_when_one_0_and_two_1s_from_circuits() {
        //given
        final var bitNo = 2;
        mockWebClientBuildRequestMethod(bitNo);
        when(circuitWebClientMock.get(BitResponse.class)).thenReturn(getOk(0))
                .thenReturn(getOk(1))
                .thenReturn(getOk(1));

        //when
        makeRequest(bitNo, 1, BitResponse.Status.OK);
        //then
        verifyBits("0", "1", "1", "1");
    }

    void should_display_1_three_1s_from_circuits() {
        //given
        final var bitNo = 3;
        mockWebClientBuildRequestMethod(bitNo);
        when(circuitWebClientMock.get(BitResponse.class)).thenReturn(getOk(1))
                .thenReturn(getOk(1))
                .thenReturn(getOk(1));
        //when
        makeRequest(bitNo, 1, BitResponse.Status.OK);
        //then
        verifyBits("1", "1", "1", "1");
    }

    void should_display_x_three_x_from_circuits() {
        //given
        final var bitNo = 4;
        mockWebClientBuildRequestMethod(bitNo);
        when(circuitWebClientMock.get(BitResponse.class)).thenReturn(getBad());
        //when
        makeRequest(bitNo, null, BitResponse.Status.ERROR);
        //then
        verifyBits("X", "X", "X", "X");
    }

    @Override
    final public void start(Stage stage) throws IOException {
        var mainWindow = new FXMLLoader(getResourceURL("FXML/voter.fxml"));
        mainWindow.setController(getController());
        Parent mainRoot = mainWindow.load();
        Scene scene = new Scene(mainRoot);
        stage.setTitle("Main");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        clickOn("#startBtn");
    }

    private void mockWebClientBuildRequestMethod(int bitNo) {
        when(circuitWebClientMock.replacePath("/bit")).thenReturn(circuitWebClientMock);
        when(circuitWebClientMock.replaceQueryParam(eq("no"), eq(bitNo))).thenReturn(circuitWebClientMock);
    }

    private VoterController getController() {
        return new VoterController(server, new WebClient[]{
                circuitWebClientMock, circuitWebClientMock, circuitWebClientMock
        }, Arrays.asList(componentTestedPort, 8001, 8002), componentTestedPort);
    }

    private void makeRequest(final int bitNo,
                             final Integer expectedBit,
                             final BitResponse.Status expectedStatus) {
        new Thread(() -> {
            var response = voterClient.replacePath("/bit").replaceQueryParam("no", bitNo).get(BitResponse.class);
            Assertions.assertThat(response).extracting(BitResponse::getStatus).isEqualTo(expectedStatus);
            Assertions.assertThat(response).extracting(BitResponse::getBitValue).isEqualTo(expectedBit);
        }).start();

    }

    private void verifyBits(String... values) {
        sleep(1000); //wait for request to perform
        FxAssert.verifyThat("#b0", TextMatchers.hasText(values[0]));
        FxAssert.verifyThat("#b1", TextMatchers.hasText(values[1]));
        FxAssert.verifyThat("#b2", TextMatchers.hasText(values[2]));
        FxAssert.verifyThat("#br", TextMatchers.hasText(values[3]));
    }

    @NotNull
    private BitResponse getOk(int val) {
        return new BitResponse(BitResponse.Status.OK, "OK", val);
    }

    @NotNull
    private BitResponse getBad() {
        return new BitResponse(BitResponse.Status.ERROR, "BAD", null);
    }

}