package pl.kielce.tu.weaii.sonb.tmr.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
}
