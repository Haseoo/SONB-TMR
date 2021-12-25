package pl.kielce.tu.weaii.sonb.tmr.circuit;

import io.javalin.http.Context;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import lombok.RequiredArgsConstructor;
import pl.kielce.tu.weaii.sonb.tmr.common.JavalinServer;
import pl.kielce.tu.weaii.sonb.tmr.common.dto.BitResponse;
import pl.kielce.tu.weaii.sonb.tmr.common.dto.Polynomial;

import java.util.ArrayList;
import java.util.List;

import static pl.kielce.tu.weaii.sonb.tmr.common.Constants.SERVER_STARTED;

@RequiredArgsConstructor
public class CircuitController {

    private final JavalinServer javalinServer;

    @FXML
    private Text equation;

    @FXML
    private Text status;

    @FXML
    private ChoiceBox<Integer> cport;

    @FXML
    private Button startBtn;

    private List<Text> bits = new ArrayList<>();

    private List<Text> sentBits = new ArrayList<>();

    @FXML
    private void onStartClick() {
        Integer selectedItem = cport.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            startServer(selectedItem);
        }
        var scene = startBtn.getScene();
        for (var i = 0; i < 8; i++) {
            var bit = (Text) scene.lookup(String.format("#bit%d", i));
            bit.setText("");
            bit.setVisible(true);
            bit.addEventFilter(MouseEvent.MOUSE_CLICKED, c -> {
                if (bit.getText().equals("1")) {
                    bit.setText("0");
                } else if (bit.getText().equals("0")) {
                    bit.setText("1");
                }
            });
            bits.add(bit);
            var sbit = (Text) scene.lookup(String.format("#sbit%d", i));
            sbit.setVisible(true);
            sbit.setText("");
            sentBits.add(sbit);
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
        javalinServer.addGetEndpoint("/bit", this::handleGetBit);
    }

    private void handleEquationEndpoint(Context context) {
        reset();
        Polynomial polynomialRequest = context.bodyAsClass(Polynomial.class);
        equation.setText(polynomialRequest.buildExpression());
        Integer root = polynomialRequest.bisection(0, 255);
        if (root == null) {
            status.setText("No real root");
            return;
        }
        String binaryRoot = new StringBuilder(Integer.toBinaryString(root)).reverse().toString();
        for (int i = 0; i < bits.size(); i++) {
            String value = binaryRoot.length() > i ? Character.toString(binaryRoot.charAt(i)) : "0";
            bits.get(i).setText(value);
        }

    }

    private void handleGetBit(Context context) {
        BitResponse bitResponse;
        String no = context.queryParam("no");
        if (no == null) {
            bitResponse = new BitResponse(BitResponse.Status.ERROR, "Provide bit no", null);
        } else {
            int index = Integer.parseInt(no);
            String bitAsString = bits.get(index).getText();
            if (bitAsString == null || bitAsString.equals("")) {
                bitResponse = new BitResponse(BitResponse.Status.ERROR, "Equation has no solution", null);
            } else {
                sentBits.get(index).setText(bitAsString);
                bitResponse = new BitResponse(BitResponse.Status.OK, "OK", Integer.parseInt(bitAsString));
            }
        }
        context.json(bitResponse);
    }

    private void reset() {
        equation.setText("");
        for (Text bit : bits) {
            bit.setText("");
        }

        for (Text bit : sentBits) {
            bit.setText("");
        }
    }
}
