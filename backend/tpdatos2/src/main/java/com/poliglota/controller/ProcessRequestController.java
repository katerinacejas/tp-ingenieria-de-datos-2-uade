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

	@GetMapping("/user/{userId}/state/{state}")
    public ResponseEntity<List<ProcessRequestDTO>> getProcessRequestByUserAndStatus(Long userId, String status) {
        return ResponseEntity.ok(processRequestService.getProcessRequestByUserAndStatus(userId, status));
    }

	@PostMapping("/{processRequestId}/approve-and-execute")
    public ResponseEntity<ExecutionHistoryDTO> approveAndExecute(Long processRequestId) {
        return ResponseEntity.ok(processRequestService.approveAndExecute(processRequestId));
    }

	@GetMapping("{state}")
    public ResponseEntity<List<ProcessRequestDTO>> getProcessRequestByStatus(String status) {
        return ResponseEntity.ok(processRequestService.getProcessRequestByStatus(status));
    }

	@GetMapping("{id}")
    public ResponseEntity<ProcessRequestDTO> getProcessRequestById(  Long id) {
        return ResponseEntity.ok(processRequestService.getProcessRequestById(id));
    }

	@GetMapping
	public ResponseEntity<List<ProcessRequestDTO>> getAll() {
		return ResponseEntity.ok(processRequestService.getAllProcessRequests());
	}

}
