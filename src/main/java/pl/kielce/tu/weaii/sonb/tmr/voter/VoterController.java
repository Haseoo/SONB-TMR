package pl.kielce.tu.weaii.sonb.tmr.voter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import lombok.RequiredArgsConstructor;
import pl.kielce.tu.weaii.sonb.tmr.common.JavalinServer;

import static pl.kielce.tu.weaii.sonb.tmr.common.Constants.SERVER_STARTED;

@RequiredArgsConstructor
public class VoterController {

    private final JavalinServer javalinServer;

    @FXML
    private Text status;

    @FXML
    private Text b0;

    @FXML
    private ChoiceBox<Integer> cport;

    @FXML
    private Button startBtn;

    @FXML
    private void onStartClick() {
        Integer selectedItem = cport.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            javalinServer.createAndStart(selectedItem);
            cport.setDisable(true);
            startBtn.setDisable(true);
            status.setText(SERVER_STARTED);
        }
    }

    @FXML
    private void initialize() {
        cport.getItems().addAll(8000, 8001, 8002);
    }

}
