package com.poliglota.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.poliglota.repository.mongo.SensorRepository;
import com.poliglota.model.mongo.Sensor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorService {
	private final SensorRepository sensorRepository;

	public Sensor create(Sensor sensor) {
		return sensorRepository.save(sensor);
	}

	public List<Sensor> getAll() {
		return sensorRepository.findAll();
	}
}
