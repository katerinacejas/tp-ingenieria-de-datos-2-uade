package com.poliglota.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.poliglota.service.SensorService;
import com.poliglota.DTO.SensorDTO;
import java.util.List;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
public class SensorController {
	private final SensorService sensorService;

	@PostMapping
	public ResponseEntity<SensorDTO> create(  SensorDTO sensorDTO) {
		return ResponseEntity.ok(sensorService.create(sensorDTO));
	}

	@GetMapping
	public ResponseEntity<List<SensorDTO>> list() {
		return ResponseEntity.ok(sensorService.getAllSensors());
	}

	@GetMapping("/{id}")
	public ResponseEntity<SensorDTO> getById (String id) {
		return ResponseEntity.ok(sensorService.getSensorById(id));
	}

	public SensorDTO updateStateSensor(String id, String estado) {
		return sensorService.toggleSensorStatus(id, estado);
	}
}
