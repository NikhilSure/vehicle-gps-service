package com.tracking.vehicle_gps_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardKpiDTO {
    private Long activeVehicles;

    private Long onlineVehicles;

    private Long offlineVehicles;

    private Long totalAlerts;

    private Double averageSpeed;

    private Long lowFuelVehicles;
}
