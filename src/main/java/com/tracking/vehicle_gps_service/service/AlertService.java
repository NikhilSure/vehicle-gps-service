    package com.tracking.vehicle_gps_service.service;

    import com.tracking.vehicle_gps_service.DTO.AlertDTO;
    import com.tracking.vehicle_gps_service.DTO.PageResponse;
    import com.tracking.vehicle_gps_service.entity.AlertEntity;
    import com.tracking.vehicle_gps_service.entity.VehicleLocationEntity;
    import com.tracking.vehicle_gps_service.repository.AlertRepository;
    import lombok.RequiredArgsConstructor;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.stereotype.Service;

    import java.util.List;

    @Service
    @RequiredArgsConstructor
    public class AlertService {
        private final AlertRepository alertRepository;

        private final VehicleTrackingService vehicleTrackingService;

        private final WebSocketService webSocketService;

        public AlertDTO convertToDTO(AlertEntity alertEntity) {
            return AlertDTO.builder().id(alertEntity.getId())
                    .alert_type(alertEntity.getAlert_type()).message(alertEntity.getMessage()).severity(alertEntity.getSeverity()).timestamp(alertEntity.getTimestamp()).read(alertEntity.isRead()).vehicleLocation(vehicleTrackingService.conevertToDTO(alertEntity.getVehicleLocationEntity())).build();
        }

        public void generateAlerts(VehicleLocationEntity vehicleLocation) {

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

                VehicleLocationEntity vehicleLocation,

                String type,

                String message,

                String severity
        ) {

            AlertEntity alertEntity =
                    new AlertEntity();

            alertEntity.setAlert_type(type);

            alertEntity.setMessage(message);

            alertEntity.setSeverity(severity);

            alertEntity.setRead(false);

            alertEntity.setTimestamp(System.currentTimeMillis());

            alertEntity.setVehicleLocationEntity(
                    vehicleLocation
            );

            webSocketService.sendAlert(convertToDTO(alertEntity));
            saveAlert(alertEntity);
        }

        public List<AlertDTO> getAllAlerts() {
            return alertRepository.findAll().stream().map(this::convertToDTO).toList();
        }

        public List<AlertDTO> getAlertBetweenTimestamp(Long startTs, Long endTs) {
            return alertRepository.findAlertsbeetweenTimstamp(startTs, endTs).stream().map(this::convertToDTO).toList();
        }

        public
        PageResponse
                <AlertDTO> getAllAlertsByOrder(
                Pageable pageable
        ) {

            Page<AlertEntity> pageData =
                    alertRepository.findAllByOrderByTimestampDesc(pageable);

            List<AlertDTO> alerts = pageData.getContent()
                    .stream()
                    .map(this::convertToDTO)
                    .toList();

            return PageResponse.<AlertDTO>builder()
                    .content(alerts)
                    .page(pageData.getNumber())
                    .size(pageData.getSize())
                    .first(pageData.isFirst())
                    .totalElements(pageData.getTotalElements())
                    .totalPages(pageData.getTotalPages())
                    .last(pageData.isLast())
                    .build();
        }

        public void saveAlert(AlertEntity alertEntity) {
            alertRepository.save(alertEntity);
        }

        public void setRead(Long alertId) {
            AlertEntity alertEntity = alertRepository.findById(alertId).orElseThrow(() -> new RuntimeException(("Alert not found")));
            alertEntity.setRead(true);
            alertRepository.save(alertEntity);
        }
    }
