package pl.kielce.tu.weaii.sonb.tmr.main;

import java.util.ArrayList;
import java.util.Arrays;

public class Polynomial {
    int[] coefficients;
    String expression;

    public Polynomial(int[] coefficients, String expression) {
        this.coefficients = coefficients;
        this.expression = expression;
    }

    public Polynomial() {
    }

    public int[] getCoefficients() {
        return coefficients;
    }

    public void setCoefficients(int[] coefficients) {
        this.coefficients = coefficients;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "Polynomial{" +
                "coefficients=" + Arrays.toString(coefficients) +
                ", expression='" + expression + '\'' +
                '}';
    }
}
