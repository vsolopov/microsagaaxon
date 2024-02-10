package com.solopov.saga.CommonService.events;

import lombok.Data;

@Data
public class OrderCanceledEvent {
    private String orderId;
    private String orderStatus = "CANCELED";
}
