package com.solopov.saga.PaymentService.command.api.events;

import com.solopov.saga.CommonService.events.PaymentCancelledEvent;
import com.solopov.saga.CommonService.events.PaymentProcessedEvent;
import com.solopov.saga.PaymentService.command.api.data.Payment;
import com.solopov.saga.PaymentService.command.api.data.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class PaymentsEventHandler {

    private final PaymentRepository paymentRepository;


    @EventHandler
    public void on(PaymentProcessedEvent event) {
        Payment payment = Payment.builder()
                .paymentId(event.getPaymentId())
                .orderId(event.getOrderId())
                .paymentStatus("COMPLETED")
                .timestamp(new Date())
                .build();

        paymentRepository.save(payment);
    }

    @EventHandler
    public void on(PaymentCancelledEvent event) {
        Payment payment = paymentRepository.findById(event.getPaymentId()).get();
        payment.setPaymentStatus(event.getGetPaymentStatus());
        paymentRepository.save(payment);
    }
}
