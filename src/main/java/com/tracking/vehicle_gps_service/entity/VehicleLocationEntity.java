package com.tracking.vehicle_gps_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vehicle_location")
@Builder
public class VehicleLocationEntity {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private  Long id;

    private String vehicleId;

    private Double lat;

    private Double lng;

    private Integer speed;

    private Integer heading;

    private Double fuelLevel;

    private boolean engineStatus;

    private long timestamp;
}
