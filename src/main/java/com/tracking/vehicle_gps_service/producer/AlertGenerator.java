package com.tracking.vehicle_gps_service.producer;

import com.tracking.vehicle_gps_service.DTO.AlertGenerationDTO;
import com.tracking.vehicle_gps_service.entity.VehicleLocationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertGenerator {
    private final KafkaTemplate<String, VehicleLocationEntity> kafkaTemplate;

    public void publish(VehicleLocationEntity location) {
        kafkaTemplate.send("alerts", location.getVehicleId(), location);
    }
}
