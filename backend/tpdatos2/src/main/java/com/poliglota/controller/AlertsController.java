package com.poliglota.controller.mongo;

import com.poliglota.model.mongo.Alerts;
import com.poliglota.service.mongo.AlertsService;
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

    // 🔹 Obtener todas las alertas
    @GetMapping
    public ResponseEntity<List<Alerts>> getAllAlerts() {
        return ResponseEntity.ok(alertsService.getAllAlerts());
    }

    // 🔹 Obtener alerta por ID
    @GetMapping("/{id}")
    public ResponseEntity<Alerts> getAlertById(@PathVariable String id) {
        return alertsService.getAlertById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Obtener alertas por tipo
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Alerts>> getAlertsByType(@PathVariable String type) {
        return ResponseEntity.ok(alertsService.getAlertsByType(type));
    }

    // 🔹 Obtener alertas por estado
    @GetMapping("/state/{state}")
    public ResponseEntity<List<Alerts>> getAlertsByState(@PathVariable String state) {
        return ResponseEntity.ok(alertsService.getAlertsByState(state));
    }

    // 🔹 Obtener alertas por sensor
    @GetMapping("/sensor/{sensorId}")
    public ResponseEntity<List<Alerts>> getAlertsBySensor(@PathVariable String sensorId) {
        return ResponseEntity.ok(alertsService.getAlertsBySensor(sensorId));
    }

    // 🔹 Obtener alertas entre fechas
    @GetMapping("/between")
    public ResponseEntity<List<Alerts>> getAlertsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(alertsService.getAlertsByDateRange(start, end));
    }

    // 🔹 Crear o actualizar una alerta
    @PostMapping
    public ResponseEntity<Alerts> saveAlert(@RequestBody Alerts alert) {
        return ResponseEntity.ok(alertsService.saveAlert(alert));
    }

    // 🔹 Actualizar estado de una alerta
    @PutMapping("/{id}/state")
    public ResponseEntity<Alerts> updateAlertState(
            @PathVariable String id,
            @RequestParam String newState) {
        return ResponseEntity.ok(alertsService.updateAlertState(id, newState));
    }

    // 🔹 Eliminar alerta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlert(@PathVariable String id) {
        alertsService.deleteAlert(id);
        return ResponseEntity.noContent().build();
    }
}
