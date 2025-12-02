package com.poliglota.service;

import com.poliglota.model.mongo.Sensor;
import com.poliglota.DTO.SensorDTO;
import com.poliglota.repository.mongo.SensorRepository;

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
    public List<SensorDTO> getAllSensors() {
        return sensorRepository.findAll().stream()
		.map(sensor -> toDto(sensor))
		.toList();
    }

	public SensorDTO create(SensorDTO sensor) {
		return toDto(sensorRepository.save(toEntity(sensor)));
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
    public List<Sensor> getSensorsByActive(String estado) {
        return sensorRepository.findByEstado(estado);
    }

    //  Crear o actualizar un sensor
    public Sensor saveSensor(Sensor sensor) {
        if (sensor.getStartDate() == null) {
            sensor.setStartDate(LocalDateTime.now());
        }
        return sensorRepository.save(sensor);
    }

    //  Activar o desactivar un sensor
    public Sensor toggleSensorStatus(String id, String estado) {
        Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sensor no encontrado con ID: " + id));
        sensor.setEstado(estado);
        return sensorRepository.save(sensor);
    }

    //  Eliminar sensor
    public void deleteSensor(String id) {
        sensorRepository.deleteById(id);
    }

	private Sensor toEntity(SensorDTO sensorDTO) {
		Sensor sensor = new Sensor();
		sensor.setName(sensorDTO.getName());
		sensor.setType(sensorDTO.getType());
		sensor.setLatitud(sensorDTO.getLatitud());
		sensor.setLongitud(sensorDTO.getLongitud());
		sensor.setCity(sensorDTO.getCity());
		sensor.setCountry(sensorDTO.getCountry());
		sensor.setEstado(sensorDTO.getEstado());
		sensor.setStartDate(sensorDTO.getStartDate());
		return sensor;
	}

	private SensorDTO toDto(Sensor sensor){
		SensorDTO sensorDTO = new SensorDTO();
		sensorDTO.setName(sensor.getName());
		sensorDTO.setType(sensor.getType());
		sensorDTO.setLatitud(sensor.getLatitud());
		sensorDTO.setLongitud(sensor.getLongitud());
		sensorDTO.setCity(sensor.getCity());
		sensorDTO.setCountry(sensor.getCountry());
		sensorDTO.setEstado(sensor.getEstado());
		sensorDTO.setStartDate(sensor.getStartDate());
		return sensorDTO;
	}
}
