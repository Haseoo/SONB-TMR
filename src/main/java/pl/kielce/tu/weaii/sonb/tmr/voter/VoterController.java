package pl.kielce.tu.weaii.sonb.tmr.voter;

import io.javalin.http.Context;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.jaxrs.client.WebClient;
import pl.kielce.tu.weaii.sonb.tmr.common.JavalinServer;
import pl.kielce.tu.weaii.sonb.tmr.common.dto.BitResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static pl.kielce.tu.weaii.sonb.tmr.common.Constants.SERVER_STARTED;
import static pl.kielce.tu.weaii.sonb.tmr.common.Utils.doMajorityVote;

@RequiredArgsConstructor
@Slf4j
public class VoterController {
    private final JavalinServer javalinServer;

    private final WebClient[] circuitClients;

    private final Collection<Integer> ports;

    private final int defaultPort;

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


    @FXML
    private void onStartClick() {
        Integer selectedItem = cport.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            javalinServer.createAndStart(selectedItem);
            javalinServer.addGetEndpoint("/bit", this::handleGetBit);
            cport.setDisable(true);
            startBtn.setDisable(true);
            status.setText(SERVER_STARTED);
        }
    }

    @FXML
    private void initialize() {
        cport.getItems().addAll(ports);
        cport.setValue(defaultPort);
        bits.add(b0);
        bits.add(b1);
        bits.add(b2);
    }

    private void handleGetBit(Context context) {
        BitResponse bitResponse = getBitResponse(context);
        context.json(bitResponse);
        br.setText(bitResponse.getBitValue() != null ? bitResponse.getBitValue().toString() : "X");
    }

    private BitResponse getBitResponse(Context context) {
        String no = context.queryParam("no");
        if (no == null) {

            return new BitResponse(BitResponse.Status.ERROR, "Provide bit no", null);
        }
        int index = Integer.parseInt(no);
        var bitResponses = new ArrayList<BitResponse>();
        for (int i = 0; i < circuitClients.length; i++) {
            var circuitClient = circuitClients[i];
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
        return doMajorityVote(bitResponses);
    }

}
