package com.tracking.vehicle_gps_service.consumer;

import com.tracking.vehicle_gps_service.DTO.VehicleLocation;
import com.tracking.vehicle_gps_service.entity.VehicleLocationEntity;
import com.tracking.vehicle_gps_service.producer.AlertGenerator;
import com.tracking.vehicle_gps_service.service.AlertService;
import com.tracking.vehicle_gps_service.service.VehicleTrackingService;
import com.tracking.vehicle_gps_service.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class GpsLocationConsumer {

    private final VehicleTrackingService vehicleTrackingService;

    private final WebSocketService webSocketService;

    private final AlertGenerator alertGenerator;

    @KafkaListener(
            topics = "vehicle-location",
            groupId = "gps-tracker-group-1"
    )
    public void consume(VehicleLocation location) {
        log.info("GPS Received : {}", location);
        VehicleLocationEntity entity = vehicleTrackingService.updateVehicleLocation(location);
        webSocketService.sendLocation(location);
        alertGenerator.publish(entity);
    }
}
