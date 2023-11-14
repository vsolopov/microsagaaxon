package com.solopov.saga.OrderService.command.api.saga;

import com.solopov.saga.CommonService.commands.CompleteOrderCommand;
import com.solopov.saga.CommonService.commands.ShipOrderCommand;
import com.solopov.saga.CommonService.commands.ValidatePaymentCommand;
import com.solopov.saga.CommonService.events.OrderCompletedEvent;
import com.solopov.saga.CommonService.events.OrderShippedEvent;
import com.solopov.saga.CommonService.events.PaymentProcessEvent;
import com.solopov.saga.CommonService.model.User;
import com.solopov.saga.CommonService.queries.GetUserPaymentDetailsQuery;
import com.solopov.saga.OrderService.command.api.events.OrderCreatedEvent;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Saga
@Slf4j
@NoArgsConstructor
public class OrderProcessingSaga {

    @Autowired
    private transient QueryGateway queryGateway;
    @Autowired
    private transient CommandGateway commandGateway;


    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent event) {
        log.info("OrderCreatedEvent in Saga for Order id: {}", event.getOrderId());

        GetUserPaymentDetailsQuery getUserPaymentDetailsQuery = new GetUserPaymentDetailsQuery(event.getUserId());
        User user = null;
        try {
            user = queryGateway.query(
                    getUserPaymentDetailsQuery,
                    ResponseTypes.optionalInstanceOf(User.class)
            ).join().get();
        } catch (Exception e) {
            log.error(e.getMessage());
            //Start the compensating transaction
        }

        ValidatePaymentCommand validatePaymentCommand = ValidatePaymentCommand.builder()
                .cardDetails(user.getCardDetails())
                .orderId(event.getOrderId())
                .paymentId(UUID.randomUUID().toString())
                .build();

        commandGateway.sendAndWait(validatePaymentCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(PaymentProcessEvent event) {
        log.info("PaymentProcessEvent in Saga for Order id: {}", event.getOrderId());

        try {
            ShipOrderCommand command = ShipOrderCommand.builder()
                    .shipmentId(UUID.randomUUID().toString())
                    .orderId(event.getOrderId())
                    .build();
            commandGateway.sendAndWait(command);
        } catch (Exception e) {
            log.error(e.getMessage());
            //Start the compensating transaction
        }
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderShippedEvent event) {
        log.info("OrderShippedEvent in Saga for Order id: {}", event.getOrderId());

        CompleteOrderCommand command = CompleteOrderCommand.builder()
                .orderId(event.getOrderId())
                .orderStatus("APPROVED")
                .build();
        commandGateway.sendAndWait(command);
        //there is no negative scenarios - no needed try-catch
    }

    @SagaEventHandler(associationProperty = "orderId")
    @EndSaga
    public void handle(OrderCompletedEvent event) {
        log.info("OrderCompletedEvent in Saga for Order id: {}", event.getOrderId());

        //here we could send command to notify user (f.e. SendInvoiceCommand).
        // But for now, it is not necessary, and we use @EndSaga to finish whole flow.
    }
}
