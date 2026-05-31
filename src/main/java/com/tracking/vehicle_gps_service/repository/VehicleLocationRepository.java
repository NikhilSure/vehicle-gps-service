package com.tracking.vehicle_gps_service.repository;


import com.tracking.vehicle_gps_service.entity.VehicleLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VehicleLocationRepository extends JpaRepository<VehicleLocationEntity, Long>{
    public Void deleteByVehicleId(String vehicleId);
    public List<VehicleLocationEntity> findByVehicleId(String vehicleid);

    @Query(value = """
        SELECT v.*
        FROM vehicle_location v
        INNER JOIN (
            SELECT vehicle_id,
                   MAX(timestamp) AS max_timestamp
            FROM vehicle_location
            GROUP BY vehicle_id
        ) latest
        ON v.vehicle_id = latest.vehicle_id
        AND v.timestamp = latest.max_timestamp
        """,
            nativeQuery = true)
    List<VehicleLocationEntity> findLatestLocation();

    @Query(value = """
        SELECT v.*
        FROM vehicle_location v
        INNER JOIN (
            SELECT vehicle_id,
                   MAX(timestamp) AS max_timestamp
            FROM vehicle_location where timestamp between :startTs and :endTs
            GROUP BY vehicle_id
        ) latest
        ON v.vehicle_id = latest.vehicle_id
        AND v.timestamp = latest.max_timestamp
        """,
            nativeQuery = true)
    List<VehicleLocationEntity> getLatestVehicleLocationBetweenTimeStamp(Long startTs,Long endTs);


    List<VehicleLocationEntity>

    findByTimestampBetweenOrderByTimestampAsc(

            Long startTime,

            Long endTime
    );
}
