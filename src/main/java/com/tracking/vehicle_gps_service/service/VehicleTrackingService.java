package com.tracking.vehicle_gps_service.service;

import com.tracking.vehicle_gps_service.DTO.VehicleLocation;
import com.tracking.vehicle_gps_service.entity.VehicleLocationEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.tracking.vehicle_gps_service.repository.VehicleLocationRepository;


import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleTrackingService {
    private final VehicleLocationRepository vehilceLocationRepository;

    private final RedisService redisService;

    public VehicleLocation conevertToDTO(VehicleLocationEntity entity) {
        return VehicleLocation.builder().vehicleId(entity.getVehicleId())
                        .lat(entity.getLat())
                                .lng(entity.getLng())
                                        .heading(entity.getHeading())
                                                .timestamp(entity.getTimestamp())
                                                    .fuelLevel(entity.getFuelLevel())
                                                        .engineStatus(entity.isEngineStatus())
                                                            .speed(entity.getSpeed())
                                                                .build();
    }

    public VehicleLocationEntity
    convertToEntity(
            VehicleLocation vehicleLocation
    ) {

        VehicleLocationEntity entity =
                new VehicleLocationEntity();

        entity.setVehicleId(
                vehicleLocation.getVehicleId()
        );

        entity.setLat(
                vehicleLocation.getLat()
        );

        entity.setLng(
                vehicleLocation.getLng()
        );

        entity.setSpeed(
                vehicleLocation.getSpeed()
        );

        entity.setFuelLevel(
                vehicleLocation.getFuelLevel()
        );

        entity.setEngineStatus(
                vehicleLocation.isEngineStatus()
        );

        entity.setTimestamp(
                vehicleLocation.getTimestamp()
        );

        return entity;
    }

    public void updateVehicleLocation(VehicleLocation location) {
        log.info("inside updateVehicleLocation:: ");
        redisService.set("vehicle:" + location.getVehicleId(), location);
        // for active vehicles
//        redisService.addMember("vehicles:active", location.getVehicleId());
        addVehicleGPS(location);
    }


//    public List<VehicleLocation> getAllVehicles () {
//        log.info("inside getAllVehicles:: ");
//        List<String> vehicleIds  = redisService.getMembers("vehicle:active", String.class);
//
//        List<VehicleLocation> vehicles = vehicleIds.stream()
//                .map(id -> redisService.get(id, VehicleLocation.class))
//                .toList();
//
//        return vehicles;
//    }

    public VehicleLocation getVehicle(String vehicleId) {
        return redisService.get("vehicle:" + vehicleId, VehicleLocation.class);
    }

    //    DB LEVEL
    public void addVehicleGPS(VehicleLocation location) {
        VehicleLocationEntity locationEntity = VehicleLocationEntity.builder()
                .vehicleId(location.getVehicleId())
                    .lat(location.getLat())
                            .lng(location.getLng())
                                    .speed(location.getSpeed())
                                            .heading(location.getHeading())
                                                    .timestamp(location.getTimestamp())
                                                        .fuelLevel(location.getFuelLevel())
                                                            .engineStatus(location.isEngineStatus())
                                                                .build();
        vehilceLocationRepository.save(locationEntity);
    }


    public void removeVehicle(String vehicleId) {
        vehilceLocationRepository.deleteByVehicleId(vehicleId);
    }

    public List<VehicleLocation> getAllVehiclesInfoFromDB(String vehicleid) {
        return vehilceLocationRepository.findByVehicleId(vehicleid).stream().map(location -> VehicleLocation.builder()
                .vehicleId(location.getVehicleId())
                .lat(location.getLat())
                .lng(location.getLng())
                .speed(location.getSpeed())
                .heading(location.getHeading())
                .timestamp(location.getTimestamp())
                .fuelLevel(location.getFuelLevel())
                .engineStatus(location.isEngineStatus())
                .build()).toList();
    }

    public List<VehicleLocation> getAllLatestVehicleInfoFromDB() {
        return vehilceLocationRepository.findLatestLocation().stream().map(location -> VehicleLocation.builder()
                .vehicleId(location.getVehicleId())
                .lat(location.getLat())
                .lng(location.getLng())
                .speed(location.getSpeed())
                .heading(location.getHeading())
                .timestamp(location.getTimestamp())
                .fuelLevel(location.getFuelLevel())
                .engineStatus(location.isEngineStatus())
                .build()).toList();
    }
}
