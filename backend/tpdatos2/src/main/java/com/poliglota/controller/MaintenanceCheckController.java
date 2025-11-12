package com.poliglota.controller;

import com.poliglota.model.mongo.MaintenanceCheck;
import com.poliglota.service.MaintenanceCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
@RequiredArgsConstructor
public class MaintenanceCheckController {

    private final MaintenanceCheckService maintenanceCheckService;

    // 🔹 Obtener todos los chequeos
    @GetMapping
    public ResponseEntity<List<MaintenanceCheck>> getAllChecks() {
        return ResponseEntity.ok(maintenanceCheckService.getAllChecks());
    }

    // 🔹 Obtener un chequeo por ID
    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceCheck> getCheckById(@PathVariable String id) {
        return maintenanceCheckService.getCheckById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Obtener chequeos por sensor
    @GetMapping("/sensor/{sensorId}")
    public ResponseEntity<List<MaintenanceCheck>> getBySensor(@PathVariable String sensorId) {
        return ResponseEntity.ok(maintenanceCheckService.getBySensor(sensorId));
    }

    // 🔹 Obtener chequeos entre fechas
    @GetMapping("/between")
    public ResponseEntity<List<MaintenanceCheck>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(maintenanceCheckService.getByDateRange(start, end));
    }

    // 🔹 Crear o actualizar un chequeo
    @PostMapping
    public ResponseEntity<MaintenanceCheck> saveCheck(@RequestBody MaintenanceCheck check) {
        return ResponseEntity.ok(maintenanceCheckService.saveCheck(check));
    }

    // 🔹 Eliminar un chequeo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCheck(@PathVariable String id) {
        maintenanceCheckService.deleteCheck(id);
        return ResponseEntity.noContent().build();
    }
}
