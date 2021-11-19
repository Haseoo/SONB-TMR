package pl.kielce.tu.weaii.sonb.tmr.mainvoter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import lombok.RequiredArgsConstructor;
import pl.kielce.tu.weaii.sonb.tmr.common.JavalinServer;

import static pl.kielce.tu.weaii.sonb.tmr.common.Constants.SERVER_STARTED;

@RequiredArgsConstructor
public class MainVoterController {

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
        if (selectedItem != null)
            startServer(selectedItem);
    }

    @FXML
    private void initialize() {
        cport.getItems().addAll(7000);
        cport.setValue(7000);
    }

    private void startServer(Integer selectedItem) {
        javalinServer.createAndStart(selectedItem);
        cport.setDisable(true);
        startBtn.setDisable(true);
        status.setText(SERVER_STARTED);
    }

}
