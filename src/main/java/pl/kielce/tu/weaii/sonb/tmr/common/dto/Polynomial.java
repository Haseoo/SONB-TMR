package pl.kielce.tu.weaii.sonb.tmr.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
public class Polynomial {
    private List<Integer> coefficients;

    @JsonCreator
    public Polynomial(List<Integer> coefficients) {
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

    public double func(double x){
        double result = 0;
        for(int i=1; i<= coefficients.size() - 1; i++){
            result += Math.pow(x,i)*coefficients.get(i);
        }
        return result + coefficients.get(0);
    }

    static final float EPSILON = (float)0.01;

    public Integer bisection(double a, double b)
    {
        if (func(a) * func(b) >= 0)
        {
            System.out.println("You have not assumed"
                    + " right a and b");
            return null;
        }

        double c = a;
        while ((b-a) >= EPSILON)
        {
            // Find middle point
            c = (a+b)/2;

            // Check if middle point is root
            if (func(c) == 0.0)
                break;

                // Decide the side to repeat the steps
            else if (func(c)*func(a) < 0)
                b = c;
            else
                a = c;
        }


        return Math.toIntExact(Math.round(c));
    }

    @Override
    public String toString() {
        return "Polynomial{" +
                "coefficients=" + coefficients +
                ", expression='" + buildExpression() + '\'' +
                '}';
    }

    public static void main(String[] args) {
        Polynomial polynomial = new Polynomial();

        polynomial.setCoefficients(Arrays.asList(-144,0,1));

        System.out.println(Integer.toBinaryString(polynomial.bisection(0, 255)));

    }
}
