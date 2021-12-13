package pl.kielce.tu.weaii.sonb.tmr.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BitResponse {

    public enum Status {
        OK,
        ERROR
    }

    private Status status;
    private String message;
    private Integer bitValue;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BitResponse that = (BitResponse) o;
        return status == that.status && Objects.equals(bitValue, that.bitValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, bitValue);
    }
}
