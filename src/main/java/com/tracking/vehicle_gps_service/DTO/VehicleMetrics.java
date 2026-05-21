package com.tracking.vehicle_gps_service.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VehicleMetrics {

    private String vehicleId;

    private Double totalDistanceKm;

    private Double averageSpeed;

    private Double maxSpeed;

    private Long totalRecords;

    private Double latestFuelLevel;

    private Boolean engineStatus;
}