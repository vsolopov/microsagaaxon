package com.solopov.saga.CommonService.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardDetails {
    private String name;
    private String cardNumber;
    private Integer validUntilMounth;
    private Integer validUtillYear;
    private Integer cvv;
}
