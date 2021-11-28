package pl.kielce.tu.weaii.sonb.tmr.main;

import java.util.Arrays;

public class Polynomial {
    private final int[] coefficients;

    public Polynomial(int[] coefficients) {
        this.coefficients = coefficients;
    }

    public String buildExpression() {
        StringBuilder sbuilder = new StringBuilder();
        for (int i = coefficients.length - 1; i >= 0; i--) {
            if (coefficients[i] == 0) {
                continue;
            }
            if (i < coefficients.length - 1 && coefficients[i] >= 0) {
                sbuilder.append("+");
            }
            if (i == 0) {
                sbuilder.append(String.format("%s", coefficients[i]));
                continue;
            }
            if (coefficients[i] != 1) {
                sbuilder.append(String.format("%sx^%s", coefficients[i], i));
            } else {
                sbuilder.append(String.format("x^%s", i));
            }
        }
        return sbuilder.toString();
    }

    public int getCoefficient(int index) {
        return coefficients[index];
    }

    @Override
    public String toString() {
        return "Polynomial{" +
                "coefficients=" + Arrays.toString(coefficients) +
                ", expression='" + buildExpression() + '\'' +
                '}';
    }
}
