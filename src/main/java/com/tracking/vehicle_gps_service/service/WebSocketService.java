package com.tracking.vehicle_gps_service.service;

import com.tracking.vehicle_gps_service.DTO.AlertDTO;
import com.tracking.vehicle_gps_service.DTO.VehicleLocation;
import com.tracking.vehicle_gps_service.entity.AlertEntity;
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

    public void sendAlert(AlertDTO alertDTO) {
        messagingTemplate.convertAndSend("/topics/alerts", alertDTO);
    }
}
