package com.solopov.saga.ShipmentService.command.api.data;

import org.springframework.data.repository.CrudRepository;

public interface ShipmentRepository extends CrudRepository<Shipment, String> {
}
