package com.solopov.saga.OrderService.command.api.aggregate;

import com.solopov.saga.CommonService.commands.CompleteOrderCommand;
import com.solopov.saga.CommonService.events.OrderCompletedEvent;
import com.solopov.saga.OrderService.command.api.command.CreateOrderCommand;
import com.solopov.saga.OrderService.command.api.events.OrderCreatedEvent;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
@NoArgsConstructor
@Slf4j
public class  OrderAggregate {


    @AggregateIdentifier
    private String orderId;

    private String productId;
    private String userId;
    private String addressId;
    private Integer quantity;
    private String orderStatus;


    @CommandHandler
    public OrderAggregate(CreateOrderCommand command) {
        log.info("CreateOrderCommand receipt in OrderAggregate CommandHandler");
        //validate the command
        OrderCreatedEvent event = new OrderCreatedEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent event) {
        log.info("OrderCreatedEvent receipt in OrderAggregate EventSourcingHandler");
        this.orderId = event.getOrderId();
        this.orderStatus = event.getOrderStatus();
        this.userId = event.getUserId();
        this.quantity = event.getQuantity();
        this.productId = event.getProductId();
        this.addressId = event.getAddressId();
    }

    @CommandHandler
    public void on(CompleteOrderCommand command) {
        log.info("CompleteOrderCommand receipt in OrderAggregate CommandHandler");
        //validate the command
        OrderCompletedEvent event = OrderCompletedEvent.builder()
                .orderId(command.getOrderId())
                .orderStatus(command.getOrderStatus())
                .build();

        //publish order completed event
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(OrderCompletedEvent event){
        log.info("OrderCompletedEvent receipt in OrderAggregate EventSourcingHandler");
        this.orderStatus = event.getOrderStatus();

    }
}
