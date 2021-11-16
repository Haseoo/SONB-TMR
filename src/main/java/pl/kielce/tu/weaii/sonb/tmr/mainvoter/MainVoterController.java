package pl.kielce.tu.weaii.sonb.tmr.mainvoter;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import lombok.RequiredArgsConstructor;
import pl.kielce.tu.weaii.sonb.tmr.common.JavalinServer;

@RequiredArgsConstructor
public class MainVoterController {

    private final JavalinServer javalinServer;

    @FXML
    private Text b0;

    @FXML
    private void initialize() {
        javalinServer.createAndStart(7000);
    }


}
