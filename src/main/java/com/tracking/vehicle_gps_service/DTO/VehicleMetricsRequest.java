package com.tracking.vehicle_gps_service.DTO;

import lombok.Data;

@Data
public class VehicleMetricsRequest {
    private long startDate;

    private long endDate;
}
