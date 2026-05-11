package com.tracking.vehicle_gps_service.service;

import com.tracking.vehicle_gps_service.DTO.VehicleLocation;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public void sendLocation(VehicleLocation location) {
        messagingTemplate.convertAndSend("/topics/vehicleLocations", location);
    }
}
