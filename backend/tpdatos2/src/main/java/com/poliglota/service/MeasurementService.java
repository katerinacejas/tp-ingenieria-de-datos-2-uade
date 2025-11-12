package com.poliglota.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.poliglota.DTO.MeasurementDTO;
import com.poliglota.model.cassandra.Measurement;
import com.poliglota.model.cassandra.MeasurementKey;
import com.poliglota.repository.cassandra.MeasurementRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeasurementService {
	private final MeasurementRepository measurementRepository;

	public MeasurementDTO save(MeasurementDTO measurementDTO) {
		return this.toDto(measurementRepository.save(this.toEntity(measurementDTO)));
	}

	public List<MeasurementDTO> getBySensor(String sensorId) {
		return measurementRepository.findByKeySensorId(sensorId)
			.stream()
			.map(measurement -> this.toDto(measurement))
			.toList();
	}
	
	private Measurement toEntity(MeasurementDTO dto) {
		Measurement measurement = new Measurement();
		MeasurementKey key = new MeasurementKey();
		key.setSensorId(dto.sensorId);
		key.setTimestamp(dto.timestamp);
		measurement.setKey(key);
		measurement.setTemperature(dto.temperature);
		measurement.setHumidity(dto.humidity);
		return measurement;
	}

	private MeasurementDTO toDto(Measurement measurement) {
		MeasurementDTO dto = new MeasurementDTO();
		dto.sensorId = measurement.getKey().getSensorId();
		dto.timestamp = measurement.getKey().getTimestamp();
		dto.temperature = measurement.getTemperature();
		dto.humidity = measurement.getHumidity();
		return dto;
	}
}
