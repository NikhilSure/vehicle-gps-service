package com.tracking.vehicle_gps_service.service;

import com.tracking.vehicle_gps_service.DTO.VehicleLocation;
import com.tracking.vehicle_gps_service.DTO.VehicleMetrics;
import com.tracking.vehicle_gps_service.entity.VehicleLocationEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.tracking.vehicle_gps_service.repository.VehicleLocationRepository;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleTrackingService {
    private final VehicleLocationRepository vehilceLocationRepository;

    private final RedisService redisService;


    public VehicleLocation conevertToDTO(VehicleLocationEntity entity) {
        return VehicleLocation.builder().vehicleId(entity.getVehicleId()).lat(entity.getLat()).lng(entity.getLng()).heading(entity.getHeading()).timestamp(entity.getTimestamp()).fuelLevel(entity.getFuelLevel()).engineStatus(entity.isEngineStatus()).speed(entity.getSpeed()).build();
    }

    public VehicleLocationEntity convertToEntity(VehicleLocation vehicleLocation) {

        VehicleLocationEntity entity = new VehicleLocationEntity();

        entity.setVehicleId(vehicleLocation.getVehicleId());

        entity.setLat(vehicleLocation.getLat());

        entity.setLng(vehicleLocation.getLng());

        entity.setSpeed(vehicleLocation.getSpeed());

        entity.setFuelLevel(vehicleLocation.getFuelLevel());

        entity.setEngineStatus(vehicleLocation.isEngineStatus());

        entity.setTimestamp(vehicleLocation.getTimestamp());

        return entity;
    }

    public VehicleLocationEntity updateVehicleLocation(VehicleLocation location) {
        log.info("inside updateVehicleLocation:: ");
        redisService.set("vehicle:" + location.getVehicleId(), location);
        // for active vehicles
//        redisService.addMember("vehicles:active", location.getVehicleId());
        return addVehicleGPS(location);
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
    public VehicleLocationEntity addVehicleGPS(VehicleLocation location) {
        VehicleLocationEntity locationEntity = VehicleLocationEntity.builder().vehicleId(location.getVehicleId()).lat(location.getLat()).lng(location.getLng()).speed(location.getSpeed()).heading(location.getHeading()).timestamp(location.getTimestamp()).fuelLevel(location.getFuelLevel()).engineStatus(location.isEngineStatus()).build();
        return vehilceLocationRepository.save(locationEntity);
    }


    public void removeVehicle(String vehicleId) {
        vehilceLocationRepository.deleteByVehicleId(vehicleId);
    }

    public List<VehicleLocation> getAllVehiclesInfoFromDB(String vehicleid) {
        return vehilceLocationRepository.findByVehicleId(vehicleid).stream().map(location -> VehicleLocation.builder().vehicleId(location.getVehicleId()).lat(location.getLat()).lng(location.getLng()).speed(location.getSpeed()).heading(location.getHeading()).timestamp(location.getTimestamp()).fuelLevel(location.getFuelLevel()).engineStatus(location.isEngineStatus()).build()).toList();
    }

    public List<VehicleLocation> getAllLatestVehicleInfoFromDB() {
        return vehilceLocationRepository.findLatestLocation().stream().map(location -> VehicleLocation.builder().vehicleId(location.getVehicleId()).lat(location.getLat()).lng(location.getLng()).speed(location.getSpeed()).heading(location.getHeading()).timestamp(location.getTimestamp()).fuelLevel(location.getFuelLevel()).engineStatus(location.isEngineStatus()).build()).toList();
    }


    private double calculateTotalDistance(

            List<VehicleLocationEntity> locations) {

        if (locations.size() < 2) {

            return 0;
        }

        double totalDistance = 0;

        for (int i = 1; i < locations.size(); i++) {

            VehicleLocationEntity previous =

                    locations.get(i - 1);

            VehicleLocationEntity current =

                    locations.get(i);

            totalDistance += calculateDistanceKm(

                    previous.getLat(), previous.getLng(),

                    current.getLat(), current.getLng());
        }

        return totalDistance;
    }

    private double calculateDistanceKm(

            double lat1,

            double lon1,

            double lat2,

            double lon2) {

        final int EARTH_RADIUS = 6371;

        double latDistance =

                Math.toRadians(lat2 - lat1);

        double lonDistance =

                Math.toRadians(lon2 - lon1);

        double a =

                Math.sin(latDistance / 2) * Math.sin(latDistance / 2)

                        +

                        Math.cos(Math.toRadians(lat1))

                                *

                                Math.cos(Math.toRadians(lat2))

                                *

                                Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c =

                2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    public List<VehicleMetrics> genAllVehicleMetrics(long startOfDay, long endOfDay) {
        List<VehicleLocationEntity> allLocations =

                vehilceLocationRepository

                        .findByTimestampBetweenOrderByTimestampAsc(

                                startOfDay,

                                endOfDay);

        Map<String, List<VehicleLocationEntity>> groupedLocations =

                allLocations.stream()

                        .collect(

                                Collectors.groupingBy(

                                        VehicleLocationEntity::getVehicleId));

        List<VehicleMetrics> metrics = new ArrayList<>();

        groupedLocations.forEach(

                (vehicleId, locations) -> {

                    double totalDistance =

                            calculateTotalDistance(locations);

                    double averageSpeed =

                            locations.stream()

                                    .mapToDouble(VehicleLocationEntity::getSpeed)

                                    .average()

                                    .orElse(0);

                    double maxSpeed =

                            locations.stream()

                                    .mapToDouble(VehicleLocationEntity::getSpeed)

                                    .max()

                                    .orElse(0);

                    VehicleLocationEntity latest =

                            locations.get(locations.size() - 1);

                    metrics.add(

                            VehicleMetrics

                                    .builder()

                                    .vehicleId(vehicleId)

                                    .totalDistanceKm(totalDistance)

                                    .averageSpeed(averageSpeed)

                                    .maxSpeed(maxSpeed)

                                    .totalRecords((long) locations.size())

                                    .latestFuelLevel(latest.getFuelLevel())

                                    .engineStatus(latest.isEngineStatus())

                                    .build());
                });

        return metrics;
    }

    public List<VehicleLocation> getLatestVehicleLocationBetweenTimeStamp(Long startTs, Long endTs) {
        return vehilceLocationRepository.getLatestVehicleLocationBetweenTimeStamp(startTs, endTs).stream().map(this::conevertToDTO).toList();
    }

    public List<VehicleLocation> getLocationsBetweenTimestamp(Long starts, Long endTs) {
        return vehilceLocationRepository.findByTimestampBetween(starts, endTs).stream().map(this::conevertToDTO).toList();
    }

    public List<String> getActiveVehicles(Long startTs, Long endTs) {
        return vehilceLocationRepository.findActiveVehicleBetweenTimestamp(startTs, endTs);
    }
}
