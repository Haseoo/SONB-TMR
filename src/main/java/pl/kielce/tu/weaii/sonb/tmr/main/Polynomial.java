package pl.kielce.tu.weaii.sonb.tmr.main;

import java.util.Arrays;

public class Polynomial {
    int[] coefficients;
    String expression;

    public Polynomial(int[] coefficients, String expression) {
        this.coefficients = coefficients;
        this.expression = expression;
    }

    public void buildExpression(){
        StringBuilder sbuilder = new StringBuilder();
        for(int i=coefficients.length - 1; i>=0; i--){
            if(i<coefficients.length - 1 && coefficients[i]>=0){
                sbuilder.append("+");
            }
            if(i==0){
                sbuilder.append(String.format("%s",coefficients[i]));
                continue;
            }
            sbuilder.append(String.format("%sx^%s",coefficients[i],i));
        }
        this.expression = sbuilder.toString();
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
