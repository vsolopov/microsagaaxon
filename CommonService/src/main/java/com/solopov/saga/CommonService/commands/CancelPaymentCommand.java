package com.solopov.saga.CommonService.commands;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
public class CancelPaymentCommand {
    @TargetAggregateIdentifier
    private String paymentId;
    private String orderId;
    private String getPaymentStatus = "CANCELED";
}
