package com.poliglota.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

import com.poliglota.DTO.ExecutionHistoryDTO;
import com.poliglota.DTO.ProcessRequestDTO;
import com.poliglota.service.ProcessRequestService;

@RestController
@RequestMapping("/api/process-requests")
@RequiredArgsConstructor
public class ProcessRequestController {
	private final ProcessRequestService processRequestService;

	@PostMapping
	public ResponseEntity<ProcessRequestDTO> createProcessRequest(  ProcessRequestDTO processRequestRequestDTO) {
		return ResponseEntity.ok(processRequestService.createProcessRequest(processRequestRequestDTO));
	}

	@PutMapping("/update-status")
	public ResponseEntity<ProcessRequestDTO> updateStatusProcessRequest(  ProcessRequestDTO processRequestRequestDTO) {
		return ResponseEntity.ok(processRequestService.updateStatusProcessRequest(processRequestRequestDTO));
	}

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ProcessRequestDTO>> getProcessRequestByUser(  Long userId) {
        return ResponseEntity.ok(processRequestService.getProcessRequestByUser(userId));
    }

	@GetMapping
	public ResponseEntity<List<ProcessRequestDTO>> getAll() {
		return ResponseEntity.ok(processRequestService.getAllProcessRequests());
	}

	@GetMapping("/{processRequestId}/execution-history")
	public ResponseEntity<List<ExecutionHistoryDTO>> getExecutionHistoryByProcessRequestId(  String processRequestId) {
		return ResponseEntity.ok(processRequestService.getExecutionHistoryByProcessRequestId(processRequestId));
	}

}
