package com.tracking.vehicle_gps_service.consumer;

import com.tracking.vehicle_gps_service.DTO.VehicleLocation;
import com.tracking.vehicle_gps_service.entity.VehicleLocationEntity;
import com.tracking.vehicle_gps_service.service.AlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AlertConsumer {
    private final AlertService alertService;

    @KafkaListener(topics = "alerts", groupId = "alert-group-1")
    public void consume(VehicleLocationEntity location) {
        log.info("GPS Received : {} for alerts", location);
        alertService.generateAlerts(location);
    }
}
