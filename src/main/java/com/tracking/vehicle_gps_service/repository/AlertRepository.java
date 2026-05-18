package com.tracking.vehicle_gps_service.repository;

import com.tracking.vehicle_gps_service.entity.AlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository extends JpaRepository<AlertEntity, Long> {
}
