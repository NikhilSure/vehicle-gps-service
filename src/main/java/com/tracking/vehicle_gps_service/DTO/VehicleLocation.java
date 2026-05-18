package com.tracking.vehicle_gps_service.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class VehicleLocation {
    private String vehicleId;

    private Double lat;

    private Double lng;

    private Integer speed;

    private Integer heading;
    private Double fuelLevel;

    private boolean engineStatus;

    private Long timestamp;
}
