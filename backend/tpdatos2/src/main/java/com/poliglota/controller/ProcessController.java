package com.poliglota.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.poliglota.service.ProcessService;
import com.poliglota.DTO.ProcessDTO;
import java.util.List;

@RestController
@RequestMapping("/api/processes")
@RequiredArgsConstructor
public class ProcessController {
	private final ProcessService processService;

	@GetMapping
    public ResponseEntity<List<ProcessDTO>> getAll() {
        return ResponseEntity.ok(processService.getAllProcesses());
    }

	@GetMapping("/{id}")
	public ResponseEntity<ProcessDTO> getById(@PathVariable Long id) {
		return ResponseEntity.ok(processService.getProcessById(id));
	}

	@GetMapping("/by-name/{name}")
	public ResponseEntity<ProcessDTO> getByName(@PathVariable String name) {
		return ResponseEntity.ok(processService.getProcessByName(name).orElse(null));
	}

	@GetMapping("/by-type/{type}")
	public ResponseEntity<List<ProcessDTO>> getByType(@PathVariable String type) {
		return ResponseEntity.ok(processService.getProcessesByType(type));
	}

	@GetMapping("/by-max-cost/{maxCost}")
	public ResponseEntity<List<ProcessDTO>> getByMaxCost(@PathVariable double maxCost) {
		return ResponseEntity.ok(processService.getProcessesByMaxCost(maxCost));
	}

	@PostMapping
	public ResponseEntity<ProcessDTO> create(@RequestBody ProcessDTO processDTO) {
		return ResponseEntity.ok(processService.saveProcess(processDTO));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		if (processService.deleteProcess(id)) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	
}
