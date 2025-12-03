package com.poliglota.service;

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

    public List<MaintenanceCheck> getAllChecks() {
        return maintenanceCheckRepository.findAll();
    }

    public Optional<MaintenanceCheck> getCheckById(String id) {
        return maintenanceCheckRepository.findById(id);
    }

    public List<MaintenanceCheck> getBySensor(String sensorId) {
        return maintenanceCheckRepository.findBySensorId(sensorId);
    }

    public List<MaintenanceCheck> getByDateRange(LocalDateTime start, LocalDateTime end) {
        return maintenanceCheckRepository.findByReviewDateBetween(start, end);
    }

    public MaintenanceCheck saveCheck(MaintenanceCheck check) {
        if (check.getReviewDate() == null) {
            check.setReviewDate(LocalDateTime.now());
        }
        return maintenanceCheckRepository.save(check);
    }

    public void deleteCheck(String id) {
        maintenanceCheckRepository.deleteById(id);
    }
}
