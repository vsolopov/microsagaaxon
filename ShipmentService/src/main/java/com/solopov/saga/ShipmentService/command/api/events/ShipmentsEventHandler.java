package com.solopov.saga.ShipmentService.command.api.events;

import com.solopov.saga.CommonService.events.OrderShippedEvent;
import com.solopov.saga.ShipmentService.command.api.data.Shipment;
import com.solopov.saga.ShipmentService.command.api.data.ShipmentRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShipmentsEventHandler {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @EventHandler
    public void on(OrderShippedEvent event) {
        Shipment shipment = new Shipment();
        BeanUtils.copyProperties(event, shipment);
        shipmentRepository.save(shipment);
    }
}



