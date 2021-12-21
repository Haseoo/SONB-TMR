package pl.kielce.tu.weaii.sonb.tmr.main;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.jaxrs.client.WebClient;
import pl.kielce.tu.weaii.sonb.tmr.common.ClientBuilder;
import pl.kielce.tu.weaii.sonb.tmr.common.dto.BitResponse;
import pl.kielce.tu.weaii.sonb.tmr.common.dto.Polynomial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class MainController {

    MainController(){
        this.voterClient = new ClientBuilder().host(VOTER_IP).port(7000).timeout(9000).build();
        this.circuitClients = new WebClient[]{
                new ClientBuilder().host(CLIENT_IP).port(9000).build(),
                new ClientBuilder().host(CLIENT_IP).port(9001).build(),
                new ClientBuilder().host(CLIENT_IP).port(9002).build()
        };
    }

    public MainController(WebClient[] circuitClients, WebClient voterClient) {
        this.circuitClients = circuitClients;
        this.voterClient = voterClient;
    }

    private final WebClient[] circuitClients;

    private final WebClient voterClient;
    private static final String CLIENT_IP = "localhost";
    private static final String VOTER_IP = "localhost";

    @FXML
    private Button startBtn;

    @FXML
    private Text status;

    private int bitNo = 0;

    @FXML
    private Button polyn;

    @FXML
    private TextField equationInput;

    private List<Text> bits = new ArrayList<>();


    private Polynomial polynomial;

    @FXML
    private void initialize() {
        reset();
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
        status.setText("Equation sent");
        startBtn.setText("Next bit");
        startBtn.setOnAction(ac -> {
            nextBit();
            ac.consume();
        });
    }

    private void startCircuits() {
        for (WebClient circuitClient : circuitClients) {
            try {
                circuitClient.replacePath("/equation").post(polynomial);
            } catch (Exception ignored) {
                //ignored
            }
        }
    }

    private void nextBit() {
        try {
            var response = voterClient.replacePath("/bit").replaceQueryParam("no", bitNo).get(BitResponse.class);
            Text bit = bits.get(bitNo);
            bit.setVisible(true);
            if(response.getStatus().equals(BitResponse.Status.ERROR)) {
                new Alert(Alert.AlertType.ERROR, response.getMessage()).showAndWait();
                reset();
                return;
            }
            bit.setText(response.getBitValue().toString());
            bitNo++;
            if (bitNo == 8) {
                startBtn.setText("Reset");
                startBtn.setOnAction(ac -> {
                    reset();
                    ac.consume();
                });
            }
        } catch (Throwable e) {
            log.error("A critical error has ocurred", e);
            new Alert(Alert.AlertType.ERROR, "A critical error has occurred").showAndWait();
            reset();
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
        status.setText("Provide equation");
        for (Text bit : bits) {
            bit.setText("");
        }
        bitNo = 0;
        startBtn.setText("Start");
        startBtn.setOnAction(ec -> {
            onStart();
            ec.consume();
        });
    }


}
