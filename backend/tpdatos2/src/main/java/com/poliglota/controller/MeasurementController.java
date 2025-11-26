package com.poliglota.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.poliglota.service.MeasurementService;
import com.poliglota.DTO.MeasurementDTO;
import java.util.List;

@RestController
@RequestMapping("/api/measurements")
@RequiredArgsConstructor
public class MeasurementController {
	private final MeasurementService measurementService;

	@PostMapping
	public ResponseEntity<MeasurementDTO> create(  MeasurementDTO measurementDTO) {
		return ResponseEntity.ok(measurementService.save(measurementDTO));
	}

	@GetMapping("/{sensorId}")
	public ResponseEntity<List<MeasurementDTO>> getBySensor(  String sensorId) {
		return ResponseEntity.ok(measurementService.getBySensor(sensorId));
	}
}
