package com.tracking.vehicle_gps_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@SpringBootApplication
@EnableWebSocketMessageBroker
public class VehicleGpsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(VehicleGpsServiceApplication.class, args);
	}

}
