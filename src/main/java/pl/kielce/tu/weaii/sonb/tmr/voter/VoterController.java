package pl.kielce.tu.weaii.sonb.tmr.voter;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import lombok.RequiredArgsConstructor;
import pl.kielce.tu.weaii.sonb.tmr.common.JavalinServer;

@RequiredArgsConstructor
public class VoterController {

    private final JavalinServer javalinServer;

    @FXML
    private Text b0;

    @FXML
    private ComboBox<Integer> cport;


    @FXML
    private void onStartClick() {
        Integer selectedItem = cport.getSelectionModel().getSelectedItem();
        if(selectedItem != null)
            javalinServer.createAndStart(selectedItem);
    }

}
