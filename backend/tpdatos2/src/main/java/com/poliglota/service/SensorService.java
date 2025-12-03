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

    public List<SensorDTO> getAllSensors() {
        return sensorRepository.findAll().stream()
		.map(sensor -> toDto(sensor))
		.toList();
    }

	public SensorDTO create(SensorDTO sensor) {
		return toDto(sensorRepository.save(toEntity(sensor)));
	}

    public SensorDTO getSensorById(String id) {
        return sensorRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Sensor no encontrado con ID: " + id));
    }

    public List<Sensor> getSensorsByType(String type) {
        return sensorRepository.findByType(type);
    }

    public List<Sensor> getSensorsByCountry(String country) {
        return sensorRepository.findByCountry(country);
    }

    public List<Sensor> getSensorsByCity(String city) {
        return sensorRepository.findByCity(city);
    }

    public List<Sensor> getSensorsByActive(String estado) {
        return sensorRepository.findByEstado(estado);
    }

    public Sensor saveSensor(Sensor sensor) {
        if (sensor.getStartDate() == null) {
            sensor.setStartDate(LocalDateTime.now());
        }
        return sensorRepository.save(sensor);
    }

    public SensorDTO toggleSensorStatus(String id, String estado) {
        Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sensor no encontrado con ID: " + id));
        sensor.setEstado(estado);
        return toDto(sensorRepository.save(sensor));
    }

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
		sensorDTO.setId(sensor.getId());
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
