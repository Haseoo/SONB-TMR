package pl.kielce.tu.weaii.sonb.tmr.circuit;

import io.javalin.http.Context;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import lombok.RequiredArgsConstructor;
import pl.kielce.tu.weaii.sonb.tmr.common.JavalinServer;
import pl.kielce.tu.weaii.sonb.tmr.common.dto.Polynomial;

import static pl.kielce.tu.weaii.sonb.tmr.common.Constants.SERVER_STARTED;

@RequiredArgsConstructor
public class CircuitController {

    private final JavalinServer javalinServer;

    @FXML
    private Text equation;

    @FXML
    private Text status;

    @FXML
    private Text b0;

    @FXML
    private ChoiceBox<Integer> cport;

    @FXML
    private Button startBtn;

    private Polynomial polynomial;

    @FXML
    private void onStartClick() {
        Integer selectedItem = cport.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            startServer(selectedItem);
        }
    }

    private void startServer(Integer selectedItem) {
        javalinServer.createAndStart(selectedItem);
        configureEndpoints();
        cport.setDisable(true);
        startBtn.setDisable(true);
        status.setText(SERVER_STARTED);
    }

    private void configureEndpoints() {
        javalinServer.addPostEndpoint("/equation", this::handleEquationEndpoint);
    }

    private void handleEquationEndpoint(Context context) {
        Polynomial polynomialRequest = context.bodyAsClass(Polynomial.class);
        this.polynomial = polynomialRequest;
        equation.setText(polynomialRequest.buildExpression());

    }


}
