package pl.kielce.tu.weaii.sonb.tmr.main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class MainController {

    private final String regex = Pattern.compile("^(\\d+)[-|\\+|\\-*|\\/](\\d+)").pattern();

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
            bits.add((Text) scene.lookup(String.format("#bit%d", i)));
        }
        bits.get(3).setVisible(true);
    }

    private static boolean check(String equation) {
        String[] numbers;
        if (equation.contains("+")) {
            numbers = equation.split("+");
        } else if (equation.contains("-")) {
            numbers = equation.split("-");
        } else if (equation.contains("*")) {
            numbers = equation.split("*");
        } else if (equation.contains("/")) {
            numbers = equation.split("/");
        } else {
            return false;
        }

        if (numbers.length != 2) {
            return false;
        }

        return isNumeric(numbers[0]) && isNumeric(numbers[1]);

    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
