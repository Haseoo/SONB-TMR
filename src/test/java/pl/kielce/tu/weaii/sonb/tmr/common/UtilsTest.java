package pl.kielce.tu.weaii.sonb.tmr.common;

import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import pl.kielce.tu.weaii.sonb.tmr.common.dto.BitResponse;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void should_return_ok__with_1_response_when_three_1() {
        //given
        var inputs = Arrays.asList(
                getOk(1), getOk(1), getOk(1)
        );
        //when
        var result = Utils.doMajorityVote(inputs);
        //then
        Assertions.assertThat(result).matches(r -> r.getStatus().equals(BitResponse.Status.OK))
                .matches(r -> r.getBitValue().equals(1));
    }

    @Test
    void should_return_ok__with_1_response_when_two_1_and_one_0() {
        //given
        var inputs = Arrays.asList(
                getOk(1), getOk(1), getOk(0)
        );
        //when
        var result = Utils.doMajorityVote(inputs);
        //then
        Assertions.assertThat(result).matches(r -> r.getStatus().equals(BitResponse.Status.OK))
                .matches(r -> r.getBitValue().equals(1));
    }

    @Test
    void should_return_ok__with_0_response_when_two_1_and_one_0() {
        //given
        var inputs = Arrays.asList(
                getOk(1), getOk(0), getOk(0)
        );
        //when
        var result = Utils.doMajorityVote(inputs);
        //then
        Assertions.assertThat(result).matches(r -> r.getStatus().equals(BitResponse.Status.OK))
                .matches(r -> r.getBitValue().equals(0));
    }

    @Test
    void should_return_ok__with_0_response_when_three_0() {
        //given
        var inputs = Arrays.asList(
                getOk(0), getOk(0), getOk(0)
        );
        //when
        var result = Utils.doMajorityVote(inputs);
        //then
        Assertions.assertThat(result).matches(r -> r.getStatus().equals(BitResponse.Status.OK))
                .matches(r -> r.getBitValue().equals(0));
    }

    @Test
    void should_return_error_when_three_errors() {
        //given
        String msg = "msg";
        var inputs = Arrays.asList(
                new BitResponse(BitResponse.Status.ERROR, msg, null),
                new BitResponse(BitResponse.Status.ERROR, msg, null),
                new BitResponse(BitResponse.Status.ERROR, msg, null)
        );
        //when
        var result = Utils.doMajorityVote(inputs);
        //then
        Assertions.assertThat(result).matches(r -> r.getStatus().equals(BitResponse.Status.ERROR))
                .matches(r -> r.getMessage().equals(msg))
                .extracting(BitResponse::getBitValue).isNull();
    }

    @NotNull
    private BitResponse getOk(int val) {
        return new BitResponse(BitResponse.Status.OK, "OK", val);
    }
}