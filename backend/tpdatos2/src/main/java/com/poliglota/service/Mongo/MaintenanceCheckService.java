package com.poliglota.service.mongo;

import com.poliglota.model.mongo.MaintenanceCheck;
import com.poliglota.repository.mongo.MaintenanceCheckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MaintenanceCheckService {

    private final MaintenanceCheckRepository maintenanceCheckRepository;

    // Obtener todos los registros
    public List<MaintenanceCheck> getAllChecks() {
        return maintenanceCheckRepository.findAll();
    }

    // Buscar por ID
    public Optional<MaintenanceCheck> getCheckById(String id) {
        return maintenanceCheckRepository.findById(id);
    }

    // Buscar por sensor
    public List<MaintenanceCheck> getBySensor(String sensorId) {
        return maintenanceCheckRepository.findBySensorId(sensorId);
    }

    // Buscar entre fechas
    public List<MaintenanceCheck> getByDateRange(LocalDateTime start, LocalDateTime end) {
        return maintenanceCheckRepository.findByReviewDateBetween(start, end);
    }

    // Crear o actualizar
    public MaintenanceCheck saveCheck(MaintenanceCheck check) {
        if (check.getReviewDate() == null) {
            check.setReviewDate(LocalDateTime.now());
        }
        return maintenanceCheckRepository.save(check);
    }

    // Eliminar
    public void deleteCheck(String id) {
        maintenanceCheckRepository.deleteById(id);
    }
}
