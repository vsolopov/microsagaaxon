package com.solopov.saga.OrderService.command.api.controller;

import com.solopov.saga.OrderService.command.api.command.CreateOrderCommand;
import com.solopov.saga.OrderService.command.api.model.OrderRestModel;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderCommandController {

    private final CommandGateway commandGateway;


    @PostMapping
    public String createOrder(@RequestBody OrderRestModel dto) {

        String orderId = UUID.randomUUID().toString();
        CreateOrderCommand command = CreateOrderCommand.builder().
                orderId(orderId)
                .addressId(dto.getAddressId())
                .productId(dto.getProductId())
                .quantity(dto.getQuantity())
                .orderStatus("CREATED")
                .build();

        commandGateway.sendAndWait(command);
        return "Order created";
    }
}
