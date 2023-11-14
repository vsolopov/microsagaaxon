package com.solopov.saga.OrderService.command.api.model;

import lombok.Data;

@Data
public class OrderRestModel {
    private String productId;
    private String userId;
    private String addressId;
    private Integer quantity;
}
