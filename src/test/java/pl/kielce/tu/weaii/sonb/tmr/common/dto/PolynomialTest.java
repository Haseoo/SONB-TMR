package pl.kielce.tu.weaii.sonb.tmr.common.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class PolynomialTest {

    @Test
    void should_resolve_equation() {
        //given
        final var sut = new Polynomial(Arrays.asList(-4, 0, 1));
        //when
        var root = sut.bisection(0, 255);
        //then
        Assertions.assertThat(root).isEqualTo(2);
    }

    @Test
    void should_return_null_when_equation_has_no_root() {
        //given
        final var sut = new Polynomial(Arrays.asList(4, 0, 1));
        //when
        var solution = sut.bisection(0, 255);
        //then
        Assertions.assertThat(solution).isNull();
    }

    @Test
    void should_construct_equation_string() {
        //given
        final var sut = new Polynomial(Arrays.asList(4, 0, 1));
        //when
        var equation = sut.buildExpression();
        //then
        Assertions.assertThat(equation).isEqualTo("x^2+4");
    }
}