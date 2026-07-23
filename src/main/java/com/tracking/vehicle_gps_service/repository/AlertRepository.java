package com.tracking.vehicle_gps_service.repository;

import com.tracking.vehicle_gps_service.entity.AlertEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlertRepository extends JpaRepository<AlertEntity, Long> {
    Page<AlertEntity> findAllByOrderByTimestampDesc(Pageable pageable);
    @Query("select a from AlertEntity a where a.timestamp between :startTs and :endTs")
    List<AlertEntity> findAlertsbeetweenTimstamp(Long startTs, Long endTs);
}
