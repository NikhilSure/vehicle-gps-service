package com.tracking.vehicle_gps_service.service;

import com.tracking.vehicle_gps_service.DTO.DashboardKpiComparisonDTO;
import com.tracking.vehicle_gps_service.DTO.DashboardKpiDTO;
import com.tracking.vehicle_gps_service.DTO.VehicleLocation;
import com.tracking.vehicle_gps_service.DTO.VehicleMetrics;
import com.tracking.vehicle_gps_service.entity.VehicleLocationEntity;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final VehicleTrackingService vehicleTrackingService;

    private final AlertService alertService;

    private double calculatePercentageChange(double current, double previous) {
        if (previous == 0) {
            return current > 0 ? 100.0 : 0.0;
        }

        return ((current - previous) / previous) * 100;
    }

    public DashboardKpiDTO getStats(Long startTs, Long endTs) {

        DashboardKpiDTO dto = new DashboardKpiDTO();

        Long activeVehicles = (long) vehicleTrackingService.getActiveVehicles(startTs, endTs).size();

        dto.setActiveVehicles(activeVehicles);

        dto.setTotalAlerts((long) alertService.getAlertBetweenTimestamp(startTs, endTs).size());

        List<VehicleMetrics> metrics = vehicleTrackingService.genAllVehicleMetrics(startTs, endTs);

        dto.setTotalDistanceTravelled(Math.round(metrics.stream().mapToDouble(VehicleMetrics::getTotalDistanceKm).sum()));

        dto.setAverageSpeed(metrics.stream().mapToDouble(VehicleMetrics::getAverageSpeed).average().orElse(0));

        dto.setLowFuelVehicles(metrics.stream().filter(m -> m.getLatestFuelLevel() < 20).count());

        return dto;
    }

    public DashboardKpiComparisonDTO generateKpiData() {

        LocalDate today = LocalDate.now();

        long todayStart = today.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

        long now = Instant.now().toEpochMilli();

        long yesterdayStart = today.minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

        long yesterdayEnd = today.atStartOfDay(ZoneId.systemDefault()).minusNanos(1).toInstant().toEpochMilli();

        DashboardKpiDTO todayStats = getStats(todayStart, now);

        DashboardKpiDTO yesterdayStats = getStats(yesterdayStart, yesterdayEnd);

        return getDashboardKpiComparisonDTO(todayStats, yesterdayStats);
    }

    private @NonNull DashboardKpiComparisonDTO getDashboardKpiComparisonDTO(DashboardKpiDTO todayStats, DashboardKpiDTO yesterdayStats) {
        DashboardKpiComparisonDTO response = new DashboardKpiComparisonDTO();

        response.setToday(todayStats);
        response.setYesterday(yesterdayStats);

        response.setActiveVehiclesChange(calculatePercentageChange(todayStats.getActiveVehicles(), yesterdayStats.getActiveVehicles()));

        response.setTotalDistanceTravelledChange(calculatePercentageChange(todayStats.getTotalDistanceTravelled(), yesterdayStats.getTotalDistanceTravelled()));

        response.setTotalAlertsChange(calculatePercentageChange(todayStats.getTotalAlerts(), yesterdayStats.getTotalAlerts()));

        response.setAverageSpeedChange(calculatePercentageChange(todayStats.getAverageSpeed(), yesterdayStats.getAverageSpeed()));

        response.setLowFuelVehiclesChange(calculatePercentageChange(todayStats.getLowFuelVehicles(), yesterdayStats.getLowFuelVehicles()));
        return response;
    }
}
