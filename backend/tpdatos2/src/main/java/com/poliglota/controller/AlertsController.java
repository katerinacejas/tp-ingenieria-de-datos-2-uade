package com.poliglota.controller;

import com.poliglota.DTO.AlertsDTO;
import com.poliglota.service.AlertsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertsController {

    private final AlertsService alertsService;

    @GetMapping
    public ResponseEntity<List<AlertsDTO>> getAllAlerts() {
        return ResponseEntity.ok(alertsService.getAllAlerts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertsDTO> getAlertById(  String id) {
        return alertsService.getAlertById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<AlertsDTO>> getAlertsByType(  String type) {
        return ResponseEntity.ok(alertsService.getAlertsByType(type));
    }

    @GetMapping("/state/{state}")
    public ResponseEntity<List<AlertsDTO>> getAlertsByState(  String state) {
        return ResponseEntity.ok(alertsService.getAlertsByState(state));
    }

    @GetMapping("/sensor/{sensorId}")
    public ResponseEntity<List<AlertsDTO>> getAlertsBySensor(  String sensorId) {
        return ResponseEntity.ok(alertsService.getAlertsBySensor(sensorId));
    }

    @GetMapping("/between")
    public ResponseEntity<List<AlertsDTO>> getAlertsByDateRange(
               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(alertsService.getAlertsByDateRange(start, end));
    }

    @PostMapping
    public ResponseEntity<AlertsDTO> createAlert(  AlertsDTO alertDTO) {
        return ResponseEntity.ok(alertsService.createAlert(alertDTO));
    }

    @PutMapping("/{id}/state")
    public ResponseEntity<AlertsDTO> updateAlertState(
              String id,
               String newState) {
        return ResponseEntity.ok(alertsService.updateAlertState(id, newState));
    }
}
