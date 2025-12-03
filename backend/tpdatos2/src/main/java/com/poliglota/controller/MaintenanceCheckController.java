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

    @GetMapping
    public ResponseEntity<List<MaintenanceCheck>> getAllChecks() {
        return ResponseEntity.ok(maintenanceCheckService.getAllChecks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceCheck> getCheckById(  String id) {
        return maintenanceCheckService.getCheckById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/sensor/{sensorId}")
    public ResponseEntity<List<MaintenanceCheck>> getBySensor(  String sensorId) {
        return ResponseEntity.ok(maintenanceCheckService.getBySensor(sensorId));
    }

    @GetMapping("/between")
    public ResponseEntity<List<MaintenanceCheck>> getByDateRange(
               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(maintenanceCheckService.getByDateRange(start, end));
    }

    @PostMapping
    public ResponseEntity<MaintenanceCheck> saveCheck(  MaintenanceCheck check) {
        return ResponseEntity.ok(maintenanceCheckService.saveCheck(check));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCheck(  String id) {
        maintenanceCheckService.deleteCheck(id);
        return ResponseEntity.noContent().build();
    }
}
