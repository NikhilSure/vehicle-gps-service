package com.tracking.vehicle_gps_service.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {
    private boolean success;

    private String message;

    private T data;

    private long timestamp;
}
