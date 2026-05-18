    package com.tracking.vehicle_gps_service.service;

    import com.tracking.vehicle_gps_service.DTO.AlertDTO;
    import com.tracking.vehicle_gps_service.DTO.VehicleLocation;
    import com.tracking.vehicle_gps_service.entity.AlertEntity;
    import com.tracking.vehicle_gps_service.repository.AlertRepository;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;

    import java.util.List;

    @Service
    @RequiredArgsConstructor
    public class AlertService {
        private final AlertRepository alertRepository;

        private final VehicleTrackingService vehicleTrackingService;

        public void generateAlerts(VehicleLocation vehicleLocation) {

            if (vehicleLocation.getSpeed() >= 100) {

                createAlert(

                        vehicleLocation,

                        "OVERSPEED",

                        "Vehicle exceeded speed limit",

                        "HIGH"
                );
            }

            if (vehicleLocation.getFuelLevel() < 20) {

                createAlert(

                        vehicleLocation,

                        "LOW_FUEL",

                        "Fuel level below 20%",

                        "MEDIUM"
                );
            }

            if (!vehicleLocation.isEngineStatus()) {

                createAlert(

                        vehicleLocation,

                        "ENGINE_OFF",

                        "Engine turned OFF",

                        "LOW"
                );
            }
        }

        private void createAlert(

                VehicleLocation vehicleLocation,

                String type,

                String message,

                String severity
        ) {

            AlertEntity alertEntity =
                    new AlertEntity();

            alertEntity.setAlert_type(type);

            alertEntity.setMessage(message);

            alertEntity.setSeverity(severity);

            alertEntity.set_read(false);

            alertEntity.setVehicleLocationEntity(

                    vehicleTrackingService
                            .convertToEntity(vehicleLocation)
            );

            saveAlert(alertEntity);
        }

        public List<AlertDTO> getAllAlerts() {
            return alertRepository.findAll().stream().map(item -> AlertDTO.builder()
                    .id(item.getId())
                    .severity(item.getSeverity())
                    .alert_type(item.getAlert_type())
                    .message(item.getMessage())
                    .timestamp(item.getTimestamp())
                    .is_read(item.is_read())
                    .vehicleLocation(vehicleTrackingService.conevertToDTO(item.getVehicleLocationEntity()))
                    .build()).toList();
        }

        public void saveAlert(AlertEntity alertEntity) {
            alertRepository.save(alertEntity);
        }

        public void setRead(Long alertId) {
            AlertEntity alertEntity = alertRepository.findById(alertId).orElseThrow(() -> new RuntimeException(("Alert not found")));
            alertEntity.set_read(true);
        }
    }
