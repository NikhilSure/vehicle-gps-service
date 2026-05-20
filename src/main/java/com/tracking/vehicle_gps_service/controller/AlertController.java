package com.tracking.vehicle_gps_service.controller;

import com.tracking.vehicle_gps_service.DTO.AlertDTO;
import com.tracking.vehicle_gps_service.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
