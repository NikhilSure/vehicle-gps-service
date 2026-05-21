package com.tracking.vehicle_gps_service.controller;

import com.tracking.vehicle_gps_service.DTO.AlertDTO;
import com.tracking.vehicle_gps_service.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AlertController
{
    public final AlertService alertService;

    @GetMapping("/orderByTS")
    public List<AlertDTO> getAlerts() {
        return alertService.getAllAlertsByOrder();
    }

    @GetMapping("/markRead/{alertId}")
    public void updateAlert(@PathVariable Long alertId) {
        alertService.setRead(alertId);
    }
}
