package com.solopov.saga.PaymentService.command.api.aggregate;

import com.solopov.saga.CommonService.commands.ValidatePaymentCommand;
import com.solopov.saga.CommonService.events.PaymentProcessEvent;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@Slf4j
@NoArgsConstructor
public class PaymentAggregate {

    @AggregateIdentifier
    private String paymentId;
    private String orderId;
    private String paymentStatus;


    @CommandHandler
    public PaymentAggregate(ValidatePaymentCommand validatePaymentCommand) {
        //validate the payment details
        // Publish the Payment Processed event
        log.info("Executong validatePaymentCommand for orderId: {} and paymentId:{}",
                validatePaymentCommand.getOrderId(), validatePaymentCommand.getPaymentId());

        PaymentProcessEvent paymentProcessEvent = new PaymentProcessEvent(
                validatePaymentCommand.getPaymentId(), validatePaymentCommand.getOrderId()
        );

        AggregateLifecycle.apply(paymentProcessEvent);
        log.info("PaymentProcessEvent applied");
    }

    @EventSourcingHandler
    public void on(PaymentProcessEvent event){
        this.paymentId = event.getPaymentId();
        this.orderId = event.getOrderId();
    }
}
