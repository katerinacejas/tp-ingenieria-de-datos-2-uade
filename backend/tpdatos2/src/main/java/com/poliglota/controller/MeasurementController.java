package com.poliglota.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.poliglota.service.MeasurementService;
import com.poliglota.model.cassandra.Measurement;
import java.util.List;

@RestController
@RequestMapping("/api/measurements")
@RequiredArgsConstructor
public class MeasurementController {
	private final MeasurementService measurementService;

	@PostMapping
	public ResponseEntity<Measurement> create(@RequestBody Measurement measurement) {
		return ResponseEntity.ok(measurementService.save(measurement));
	}

	@GetMapping("/{sensorId}")
	public ResponseEntity<List<Measurement>> getBySensor(@PathVariable String sensorId) {
		return ResponseEntity.ok(measurementService.getBySensor(sensorId));
	}
}
