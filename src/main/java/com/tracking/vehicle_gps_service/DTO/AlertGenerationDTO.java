package com.tracking.vehicle_gps_service.DTO;

import lombok.Data;

@Data
public class AlertGenerationDTO {
    private Long vehicleId;
    private Long VehicleEntityId;
    private Long timestamp;
}
