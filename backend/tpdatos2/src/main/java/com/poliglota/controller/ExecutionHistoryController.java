package com.poliglota.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import com.poliglota.DTO.ExecutionHistoryDTO;
import com.poliglota.service.ExecutionHistoryService;

import java.util.List;

@RestController
@RequestMapping("/api/process-requests")
@RequiredArgsConstructor
public class ExecutionHistoryController {
	private final ExecutionHistoryService executionHistoryService;


	@GetMapping("/{processRequestId}")
	public ResponseEntity<List<ExecutionHistoryDTO>> getExecutionHistoryByProcessRequestId(String processRequestId) {
		return ResponseEntity.ok(executionHistoryService.getExecutionHistoryByProcessRequestId(processRequestId));
	}

	public ResponseEntity<List<ExecutionHistoryDTO>> getAll() {
		return ResponseEntity.ok(executionHistoryService.getAll());
	}

}
