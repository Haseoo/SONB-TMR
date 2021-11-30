package pl.kielce.tu.weaii.sonb.tmr.main;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;
import lombok.RequiredArgsConstructor;
import org.apache.cxf.jaxrs.client.WebClient;
import pl.kielce.tu.weaii.sonb.tmr.common.ClientBuilder;
import pl.kielce.tu.weaii.sonb.tmr.common.dto.Polynomial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MainController {

    private static final String CLIENT_IP = "localhost";
    private static final String VOTER_IP = "localhost";

    private final WebClient[] circuitClients = {
            new ClientBuilder().host(CLIENT_IP).port(9000).build(),
            new ClientBuilder().host(CLIENT_IP).port(9001).build(),
            new ClientBuilder().host(CLIENT_IP).port(9002).build()
    };

    private final WebClient voterClient = new ClientBuilder().host(VOTER_IP).port(7000).build();
    @FXML
    private Button startBtn;

    @FXML
    private Button polyn;

    @FXML
    private TextField equationInput;

    private List<Text> bits = new ArrayList<>();


    private Polynomial polynomial;

    @FXML
    private void initialize() {
        equationInput.setDisable(true);
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
        startBtn.setDisable(true);
    }

    private void startCircuits() {
        for (WebClient circuitClient : circuitClients) {
            circuitClient.replacePath("/equation").post(polynomial);
        }
    }

    @FXML
    private void openPolynCreator() {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setContentText("Max exponent");
        textInputDialog.setHeaderText("Enter the max exponent");
        Integer maxPolyn = null;
        while (maxPolyn == null) {
            try {
                Optional<String> input = textInputDialog.showAndWait();
                if (input.isEmpty()) {
                    return;
                }
                int number = Integer.parseInt(input.orElse(""));
                if (number >= 0) {
                    maxPolyn = number;
                }
                textInputDialog.setHeaderText("Enter a positive integer");
            } catch (Throwable ignored) {
                textInputDialog.setHeaderText("Enter a valid integer");
            }
        }
        createDialogs(maxPolyn);
    }


    private void createDialogs(int maxPolyn) {
        ArrayList<TextInputDialog> dialogs = new ArrayList<>();
        int[] coefficients = new int[maxPolyn + 1];

        for (int i = 0; i <= maxPolyn; i++) {
            dialogs.add(i, new TextInputDialog());
            dialogs.get(i).setHeaderText(String
                    .format("Enter coefficient for x^%s", i));
            if (i == 0) {
                dialogs.get(i).setHeaderText("Enter constant number");
            }
        }

        for (int i = maxPolyn; i >= 0; i--) {
            Integer coefficient = null;
            while (coefficient == null) {
                try {
                    Optional<String> input = dialogs.get(i).showAndWait();
                    if (input.isEmpty()) {
                        return;
                    }
                    coefficient = Integer.parseInt(input.orElse(""));
                } catch (Throwable ignored) {
                    dialogs.get(i).setContentText("\nenter valid integer");
                }
                if (coefficient != null) {
                    coefficients[i] = coefficient;
                }
            }
        }
        polynomial = new Polynomial(Arrays.stream(coefficients)
                .boxed()
                .collect(Collectors.toCollection(ArrayList::new)));
        equationInput.setText(polynomial.buildExpression());
        startBtn.setDisable(false);
    }


    private void reset() {
        polynomial = null;
        equationInput.setText("");
        for (Text bit : bits) {
            bit.setText("");
        }
    }

}
