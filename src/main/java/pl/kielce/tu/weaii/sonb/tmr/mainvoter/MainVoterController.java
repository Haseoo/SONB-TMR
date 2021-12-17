package pl.kielce.tu.weaii.sonb.tmr.mainvoter;

import io.javalin.http.Context;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.jaxrs.client.WebClient;
import pl.kielce.tu.weaii.sonb.tmr.common.ClientBuilder;
import pl.kielce.tu.weaii.sonb.tmr.common.JavalinServer;
import pl.kielce.tu.weaii.sonb.tmr.common.dto.BitResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static pl.kielce.tu.weaii.sonb.tmr.common.Constants.SERVER_STARTED;

@RequiredArgsConstructor
@Slf4j
public class MainVoterController {

    private final JavalinServer javalinServer;

    private static final String VOTER_IP = "localhost";

    @FXML
    private Text status;

    @FXML
    private Text b0;

    @FXML
    private Text b1;

    @FXML
    private Text b2;

    @FXML
    private Text br;
    
    private List<Text> bits = new ArrayList<>();

    @FXML
    private ChoiceBox<Integer> cport;

    @FXML
    private Button startBtn;

    private final WebClient[] voterClients = {
            new ClientBuilder().host(VOTER_IP).port(8000).timeout(500).build(),
            new ClientBuilder().host(VOTER_IP).port(8001).timeout(500).build(),
            new ClientBuilder().host(VOTER_IP).port(8002).timeout(500).build()
    };


    @FXML
    private void onStartClick() {
        Integer selectedItem = cport.getSelectionModel().getSelectedItem();
        if (selectedItem != null)
            startServer(selectedItem);
    }

    @FXML
    private void initialize() {
        cport.getItems().addAll(7000);
        cport.setValue(7000);
        bits.add(b0);
        bits.add(b1);
        bits.add(b2);
    }

    private void startServer(Integer selectedItem) {
        javalinServer.createAndStart(selectedItem);
        javalinServer.addGetEndpoint("/bit", this::handleGetBit);
        cport.setDisable(true);
        startBtn.setDisable(true);
        status.setText(SERVER_STARTED);
    }


    private void handleGetBit(Context context) {
        BitResponse bitResponse = getBitResponse(context);
        context.json(bitResponse);
        br.setText(bitResponse.getBitValue() != null ? bitResponse.getBitValue().toString() : "X");
    }

    private BitResponse getBitResponse(Context context) {
        String no = context.queryParam("no");
        if(no == null) {
            return new BitResponse(BitResponse.Status.ERROR, "Provide bit no", null);
        }
        int index = Integer.parseInt(no);
        var bitResponses = new ArrayList<BitResponse>();
        for (int i = 0; i < voterClients.length; i++) {
            var circuitClient = voterClients[i];
            BitResponse bitResponse;
            try {
                bitResponse = circuitClient.replacePath("/bit").replaceQueryParam("no", index).get(BitResponse.class);
            } catch (Throwable e) {
                log.error("Error getbit request from client no {} \n {}", i, e);
                bitResponse = new BitResponse(BitResponse.Status.ERROR, "Request error", null);
            }
            bits.get(i).setText(bitResponse.getBitValue() != null ? bitResponse.getBitValue().toString() : "X");
            bitResponses.add(bitResponse);
        }
        var bitsResponsesMap = bitResponses.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return bitsResponsesMap.entrySet().stream()
                .filter(entry -> entry.getValue() >= 2)
                .findFirst().map(Map.Entry::getKey)
                .orElse(new BitResponse(BitResponse.Status.ERROR, "Three different results", null));
    }

}
