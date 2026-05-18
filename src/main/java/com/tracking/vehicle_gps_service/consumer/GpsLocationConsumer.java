package com.tracking.vehicle_gps_service.consumer;

import com.tracking.vehicle_gps_service.DTO.VehicleLocation;
import com.tracking.vehicle_gps_service.service.VehicleTrackingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class GpsLocationConsumer {

    private final VehicleTrackingService vehicleTrackingService;
    @KafkaListener(
            topics = "vehicle-location",
            groupId = "gps-tracker-group-1"
    )
    public void consume(VehicleLocation location) {
        log.info("GPS Received : {}", location);
        vehicleTrackingService.updateVehicleLocation(location);

    }
}
