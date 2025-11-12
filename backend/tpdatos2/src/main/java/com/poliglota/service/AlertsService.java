package com.poliglota.service;

import com.poliglota.DTO.AlertsDTO;
import com.poliglota.model.mongo.Alerts;
import com.poliglota.repository.AlertsRepository;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertsService {

    private final AlertsRepository alertsRepository;

    //  Obtener todas las alertas
    public List<AlertsDTO> getAllAlerts() {
        return alertsRepository.findAll()
			.stream()
			.map(this::toDto)
			.toList();
    }

    //  Buscar alerta por ID
    public Optional<AlertsDTO> getAlertById(String id) {
        Alerts alert =  alertsRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Alerta no encontrada con ID: " + id));
		return Optional.of(toDto(alert));
    }

    //  Buscar alertas por tipo
    public List<AlertsDTO> getAlertsByType(String type) {
        return alertsRepository.findByType(type)
			.stream()
			.map(this::toDto)
			.toList();
    }

    //  Buscar alertas por estado
    public List<AlertsDTO> getAlertsByState(String state) {
        return alertsRepository.findByState(state)
			.stream()
			.map(this::toDto)
			.toList();		
    }

    //  Buscar alertas por sensor
    public List<AlertsDTO> getAlertsBySensor(String sensorId) {
        return alertsRepository.findBySensorId(sensorId)
			.stream()
			.map(this::toDto)
			.toList();			
    }

    //  Buscar alertas en un rango de fechas
    public List<AlertsDTO> getAlertsByDateRange(LocalDateTime start, LocalDateTime end) {
        return alertsRepository.findByDatetimeBetween(start, end)
			.stream()
			.map(this::toDto)
			.toList();				
    }

    //  Crear o actualizar una alerta
    public AlertsDTO createAlert(AlertsDTO alertDTO) {
		Alerts alert = new Alerts();
		if (alertDTO.getSensorId() != null) {
			alert.setType("sensor");
			alert.setSensorId(alertDTO.getSensorId());
		}
		else {
			alert.setType("climatica");
			alert.setSensorId(null);
		}
		
		alert.setState(alertDTO.getState());
		alert.setDatetime(alertDTO.getDatetime());
		alert.setDescripction(alertDTO.getDescripction());
        return toDto(alertsRepository.save(alert));
    }

    //  Cambiar estado (ej: de activa a resuelta)
    public AlertsDTO updateAlertState(String id, String newState) {
        Alerts alert = alertsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alerta no encontrada con ID: " + id));
        alert.setState(newState);
        return toDto(alertsRepository.save(alert));
    }

	private AlertsDTO toDto(Alerts alert) {
		AlertsDTO dto = new AlertsDTO();
		if (alert.getSensorId() != null) {
			dto.setType("sensor");
			dto.setSensorId(alert.getSensorId());
		}
		else {
			dto.setType("climatica");
			dto.setSensorId(null);
		}
		dto.setAlertId(alert.getAlertId());
		dto.setState(alert.getState());
		dto.setDatetime(alert.getDatetime());
		dto.setDescripction(alert.getDescripction());
		return dto;
	}
}
