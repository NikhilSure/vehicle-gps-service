package com.tracking.vehicle_gps_service.controller;

import com.tracking.vehicle_gps_service.DTO.VehicleMetrics;
import com.tracking.vehicle_gps_service.DTO.VehicleMetricsRequest;
import com.tracking.vehicle_gps_service.service.VehicleTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;


import com.tracking.vehicle_gps_service.DTO.VehicleLocation;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@CrossOrigin("*")
public class VehicleTrackingController {

    private final VehicleTrackingService vehicleTrackingService;

    @GetMapping
    public Collection<VehicleLocation> getAllVehicles() {
        return vehicleTrackingService.getAllLatestVehicleInfoFromDB();
    }

    @GetMapping("/{vehicleId}")
    public VehicleLocation getVehicle(
            @PathVariable String vehicleId
    ) {
        return vehicleTrackingService.getVehicle(vehicleId);
    }

    @PostMapping("/vehicleMetrics")
    public List<VehicleMetrics> getAllVehicleMetricsToday(@RequestBody VehicleMetricsRequest payload
    ) {
        long startDate = payload.getStartDate();
        long endDate = payload.getEndDate();
        return vehicleTrackingService.genAllVehicleMetricsToday(startDate, endDate);
    }
}
