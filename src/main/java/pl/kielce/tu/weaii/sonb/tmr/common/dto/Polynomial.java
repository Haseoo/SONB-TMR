package pl.kielce.tu.weaii.sonb.tmr.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class Polynomial {
    private ArrayList<Integer> coefficients;

    @JsonCreator
    public Polynomial(ArrayList<Integer> coefficients) {
        this.coefficients = coefficients;
    }

    public String buildExpression() {
        StringBuilder sbuilder = new StringBuilder();
        for (int i = coefficients.size() - 1; i >= 0; i--) {
            if (coefficients.get(i) == 0) {
                continue;
            }
            if (i < coefficients.size() - 1 && coefficients.get(i) >= 0) {
                sbuilder.append("+");
            }
            if (i == 0) {
                sbuilder.append(String.format("%s", coefficients.get(i)));
                continue;
            }
            if (coefficients.get(i) != 1) {
                sbuilder.append(String.format("%sx^%s", coefficients.get(i), i));
            } else {
                sbuilder.append(String.format("x^%s", i));
            }
        }
        return sbuilder.toString();
    }

    @Override
    public String toString() {
        return "Polynomial{" +
                "coefficients=" + coefficients +
                ", expression='" + buildExpression() + '\'' +
                '}';
    }
}
