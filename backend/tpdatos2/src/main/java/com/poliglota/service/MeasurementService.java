package com.poliglota.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.poliglota.model.cassandra.Measurement;
import com.poliglota.repository.MeasurementRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeasurementService {
	private final MeasurementRepository measurementRepository;

	public Measurement save(Measurement measurement) {
		return measurementRepository.save(measurement);
	}

	public List<Measurement> getBySensor(String sensorId) {
		return measurementRepository.findByKeySensorId(sensorId);
	}
}
