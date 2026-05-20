package com.tracking.vehicle_gps_service.controller;

import com.tracking.vehicle_gps_service.DTO.DashboardKpiDTO;
import com.tracking.vehicle_gps_service.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    private  final AnalyticsService analyticsService;

    @GetMapping("/kpi")
    public DashboardKpiDTO getKpis() {
        return analyticsService.generateKpiData();
    }
}
