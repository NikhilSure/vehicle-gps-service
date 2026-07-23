package com.tracking.vehicle_gps_service.DTO;

import lombok.Data;

@Data
public class DashboardKpiComparisonDTO {

    private DashboardKpiDTO today;

    private DashboardKpiDTO yesterday;

    private Double activeVehiclesChange;
    private Double totalDistanceTravelledChange;
    private Double totalAlertsChange;
    private Double averageSpeedChange;
    private Double lowFuelVehiclesChange;
}