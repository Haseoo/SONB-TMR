package pl.kielce.tu.weaii.sonb.tmr.main;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;
import lombok.RequiredArgsConstructor;
import org.apache.cxf.jaxrs.client.WebClient;
import pl.kielce.tu.weaii.sonb.tmr.common.ClientBuilder;
import pl.kielce.tu.weaii.sonb.tmr.common.dto.EquationRequest;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    private Button polyn;

    @FXML
    private TextField equationInput;

    private List<Text> bits = new ArrayList<>();

   private TextInputDialog td = new TextInputDialog();

   private Polynomial polynomial = new Polynomial();

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

    @FXML
    private void openPolynCreator(){
        td.setContentText("Max exponent");
        td.setHeaderText("Enter the max exponent");
        td.showAndWait();
        System.out.println(td.getEditor().getText());
        int maxPolyn = Integer.parseInt(td.getEditor().getText());
        createDialogs(maxPolyn);
    }

    private void createDialogs(int maxPolyn){
        ArrayList<TextInputDialog> dialogs = new ArrayList<>();
        int coefficients[] = new int[maxPolyn + 1];

        for(int i=0; i<= maxPolyn; i++){
            dialogs.add(i,new TextInputDialog());
            dialogs.get(i).setHeaderText(String
                    .format("Enter coefficient for x^%s variable",i));
            if(i==0){
                dialogs.get(i).setHeaderText("Enter constant number");
            }
        }

        for (int i = maxPolyn; i >= 0; i--) {
            while (!numberValidator(dialogs.get(i).showAndWait().get())){
                dialogs.get(i).setHeaderText(dialogs.get(i).getHeaderText() + "\nenter valid integer");
            }
            int element = Integer.parseInt(dialogs.get(i).getEditor().getText());
            coefficients[i] = element;
        }
        polynomial.setCoefficients(coefficients);
        polynomial.buildExpression();

        System.out.println(polynomial.toString());
    }

    private boolean numberValidator(String number){
        if(number == null){
            return false;
        }
        boolean matches = Pattern.matches("^-?\\d+$",number);
        return matches;
    }

}
