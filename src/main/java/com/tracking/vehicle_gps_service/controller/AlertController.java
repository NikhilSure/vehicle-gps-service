package com.tracking.vehicle_gps_service.controller;

import com.tracking.vehicle_gps_service.DTO.AlertDTO;
import com.tracking.vehicle_gps_service.DTO.PageResponse;
import com.tracking.vehicle_gps_service.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AlertController
{
    public final AlertService alertService;

    @GetMapping
    public PageResponse
            <AlertDTO> getAllAlerts(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "timestamp")
            String sortBy
    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size
                );

        return alertService.getAllAlertsByOrder(
                pageable
        );
    }

    @GetMapping("/markRead/{alertId}")
    public void updateAlert(@PathVariable Long alertId) {
        alertService.setRead(alertId);
    }
}
