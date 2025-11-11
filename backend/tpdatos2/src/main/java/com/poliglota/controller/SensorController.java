package com.poliglota.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.poliglota.service.SensorService;
import com.poliglota.model.mongo.Sensor;
import java.util.List;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
public class SensorController {
	private final SensorService sensorService;

	@PostMapping
	public ResponseEntity<Sensor> create(@RequestBody Sensor sensor) {
		return ResponseEntity.ok(sensorService.create(sensor));
	}

	@GetMapping
	public ResponseEntity<List<Sensor>> list() {
		return ResponseEntity.ok(sensorService.getAll());
	}
}
