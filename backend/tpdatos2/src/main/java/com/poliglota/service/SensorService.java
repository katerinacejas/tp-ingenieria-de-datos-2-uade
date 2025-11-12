package com.poliglota.service;

import com.poliglota.model.mongo.Sensor;
import com.poliglota.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SensorService {

    private final SensorRepository sensorRepository;

    //  Obtener todos los sensores
    public List<Sensor> getAllSensors() {
        return sensorRepository.findAll();
    }

	public Sensor create(Sensor sensor) {
		return sensorRepository.save(sensor);
	}

    //  Obtener sensor por ID
    public Optional<Sensor> getSensorById(String id) {
        return sensorRepository.findById(id);
    }

    //  Buscar por tipo
    public List<Sensor> getSensorsByType(String type) {
        return sensorRepository.findByType(type);
    }

    //  Buscar por país
    public List<Sensor> getSensorsByCountry(String country) {
        return sensorRepository.findByCountry(country);
    }

    //  Buscar por ciudad
    public List<Sensor> getSensorsByCity(String city) {
        return sensorRepository.findByCity(city);
    }

    //  Buscar por estado (activo/inactivo)
    public List<Sensor> getSensorsByActive(boolean active) {
        return sensorRepository.findByActive(active);
    }

    //  Crear o actualizar un sensor
    public Sensor saveSensor(Sensor sensor) {
        if (sensor.getStartDate() == null) {
            sensor.setStartDate(LocalDateTime.now());
        }
        return sensorRepository.save(sensor);
    }

    //  Activar o desactivar un sensor
    public Sensor toggleSensorStatus(String id, boolean active) {
        Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sensor no encontrado con ID: " + id));
        sensor.setActive(active);
        return sensorRepository.save(sensor);
    }

    //  Eliminar sensor
    public void deleteSensor(String id) {
        sensorRepository.deleteById(id);
    }
}
