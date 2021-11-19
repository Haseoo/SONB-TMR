package pl.kielce.tu.weaii.sonb.tmr.main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import lombok.RequiredArgsConstructor;
import org.apache.cxf.jaxrs.client.WebClient;
import pl.kielce.tu.weaii.sonb.tmr.common.ClientBuilder;
import pl.kielce.tu.weaii.sonb.tmr.common.dto.EquationRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class MainController {

    private final String regex = Pattern.compile("^(\\d+)[-|\\+|\\-*|\\/](\\d+)").pattern();

    private final WebClient[] circuitClients = {
            new ClientBuilder().host("localhost").port(9000).build(),
            new ClientBuilder().host("localhost").port(9001).build(),
            new ClientBuilder().host("localhost").port(9002).build()
    };

    @FXML
    private Button startBtn;

    @FXML
    private TextField equationInput;

    private List<Text> bits = new ArrayList<>();

    @FXML
    private void initialize() {

        equationInput.setDisable(false);
        equationInput.textProperty().addListener((o, ov, nv) -> {
            startBtn.setDisable(!nv.matches(regex));
        });


    }

    @FXML
    private void onStart() {
        var scene = startBtn.getScene();
        for (var i = 0; i < 8; i++) {
            var text = (Text) scene.lookup(String.format("#bit%d", i));
            text.setText("");
            bits.add(text);
        }
        startCircuits();
        equationInput.setDisable(true);
        startBtn.setDisable(true);
    }

    private void startCircuits() {
        for (WebClient circuitClient : circuitClients) {
            circuitClient.replacePath("/equation").post(new EquationRequest(equationInput.getText()));
        }
    }


}
