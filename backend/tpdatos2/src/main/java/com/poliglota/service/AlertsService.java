package com.poliglota.service;

import com.poliglota.model.mongo.Alerts;
import com.poliglota.repository.AlertsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlertsService {

    private final AlertsRepository alertsRepository;

    // 🔹 Obtener todas las alertas
    public List<Alerts> getAllAlerts() {
        return alertsRepository.findAll();
    }

    // 🔹 Buscar alerta por ID
    public Optional<Alerts> getAlertById(String id) {
        return alertsRepository.findById(id);
    }

    // 🔹 Buscar alertas por tipo
    public List<Alerts> getAlertsByType(String type) {
        return alertsRepository.findByType(type);
    }

    // 🔹 Buscar alertas por estado
    public List<Alerts> getAlertsByState(String state) {
        return alertsRepository.findByState(state);
    }

    // 🔹 Buscar alertas por sensor
    public List<Alerts> getAlertsBySensor(String sensorId) {
        return alertsRepository.findBySensorId(sensorId);
    }

    // 🔹 Buscar alertas en un rango de fechas
    public List<Alerts> getAlertsByDateRange(LocalDateTime start, LocalDateTime end) {
        return alertsRepository.findByDatetimeBetween(start, end);
    }

    // 🔹 Crear o actualizar una alerta
    public Alerts saveAlert(Alerts alert) {
        if (alert.getDatetime() == null) {
            alert.setDatetime(LocalDateTime.now());
        }
        return alertsRepository.save(alert);
    }

    // 🔹 Cambiar estado (ej: de activa a resuelta)
    public Alerts updateAlertState(String id, String newState) {
        Alerts alert = alertsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alerta no encontrada con ID: " + id));
        alert.setState(newState);
        return alertsRepository.save(alert);
    }

    // 🔹 Eliminar una alerta
    public void deleteAlert(String id) {
        alertsRepository.deleteById(id);
    }
}
