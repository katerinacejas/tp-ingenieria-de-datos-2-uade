package com.poliglota.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

import com.poliglota.DTO.ExecutionHistoryDTO;
import com.poliglota.DTO.ProcessRequestDTO;
import com.poliglota.DTO.request.ProcessRequestRequestDTO;
import com.poliglota.service.ProcessRequestService;

@RestController
@RequestMapping("/api/process-requests")
@RequiredArgsConstructor
public class ProcessRequestController {
	private final ProcessRequestService processRequestService;

	@PostMapping
	public ResponseEntity<ProcessRequestDTO> createProcessRequest(@RequestBody ProcessRequestRequestDTO processRequestRequestDTO) {
		return ResponseEntity.ok(processRequestService.createProcessRequest(processRequestRequestDTO));
	}

	@PutMapping("/update-status")
	public ResponseEntity<ProcessRequestDTO> updateStatusProcessRequest(@RequestBody ProcessRequestRequestDTO processRequestRequestDTO) {
		return ResponseEntity.ok(processRequestService.updateStatusProcessRequest(processRequestRequestDTO));
	}

	@GetMapping
	public ResponseEntity<List<ProcessRequestDTO>> getAll() {
		return ResponseEntity.ok(processRequestService.getAllProcessRequests());
	}

	@GetMapping("/{processRequestId}/execution-history")
	public ResponseEntity<List<ExecutionHistoryDTO>> getExecutionHistoryByProcessRequestId(@PathVariable String processRequestId) {
		return ResponseEntity.ok(processRequestService.getExecutionHistoryByProcessRequestId(processRequestId));
	}

}
