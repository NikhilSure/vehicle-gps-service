package com.tracking.vehicle_gps_service.service;

import com.tracking.vehicle_gps_service.DTO.DashboardKpiDTO;
import com.tracking.vehicle_gps_service.DTO.VehicleLocation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private  final VehicleTrackingService vehicleTrackingService;

    private final AlertService alertService;

    public DashboardKpiDTO generateKpiData() {
        DashboardKpiDTO dashboardKpiDTO = new DashboardKpiDTO();

        List<VehicleLocation> latestLocations = vehicleTrackingService.getAllLatestVehicleInfoFromDB();

        long activeVehicles = (long)latestLocations.size();
        dashboardKpiDTO.setActiveVehicles(activeVehicles);

        long onlineVehicles = latestLocations.stream().filter(VehicleLocation::isEngineStatus).count();

        dashboardKpiDTO.setOnlineVehicles(onlineVehicles);

        dashboardKpiDTO.setOfflineVehicles(activeVehicles - onlineVehicles);

        dashboardKpiDTO.setTotalAlerts((long)alertService.getAllAlerts().size());

        dashboardKpiDTO.setAverageSpeed(latestLocations.stream().mapToDouble(VehicleLocation::getSpeed).average().orElse(0) / activeVehicles);

        dashboardKpiDTO.setLowFuelVehicles(latestLocations.stream().filter(location -> location.getFuelLevel() < 20).count());

        return  dashboardKpiDTO;
    }
}
