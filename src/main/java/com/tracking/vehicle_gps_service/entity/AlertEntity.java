package com.tracking.vehicle_gps_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="alerts")
@Data
public class AlertEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String alert_type;

    private String message;

    private String severity;

    private long timestamp;

    private boolean is_read;

    @ManyToOne
    @JoinColumn(name = "vehicleLocationId")
    private VehicleLocationEntity vehicleLocationEntity;
}
