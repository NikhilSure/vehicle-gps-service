package com.tracking.vehicle_gps_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class AlertDTO {
    private long id;

    private String alert_type;

    private String message;

    private String severity;

    private long timestamp;

    private boolean read;

    private VehicleLocation vehicleLocation;
}
