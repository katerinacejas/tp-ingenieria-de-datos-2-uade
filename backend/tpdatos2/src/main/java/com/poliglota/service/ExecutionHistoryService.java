package com.poliglota.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.poliglota.DTO.ExecutionHistoryDTO;
import com.poliglota.model.mysql.ExecutionHistory;
import com.poliglota.repository.jpa.ExecutionHistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExecutionHistoryService {
	private final ExecutionHistoryRepository executionHistoryRepository;

	public List<ExecutionHistoryDTO> getExecutionHistoryByProcessRequestId(String processRequestId) {
		return executionHistoryRepository.findByProcessRequest_ProcessRequestId(Long.parseLong(processRequestId))
				.stream()
				.map(executionHistory -> this.toDto(executionHistory))
				.toList();
	}

	public List<ExecutionHistoryDTO> getAll() {
		return executionHistoryRepository.findAll()
				.stream()
				.map(executionHistory -> this.toDto(executionHistory))
				.toList();
	}
	
	private ExecutionHistoryDTO toDto(ExecutionHistory executionHistory) {
		ExecutionHistoryDTO dto = new ExecutionHistoryDTO();
		dto.setExecutionId(executionHistory.getExecutionId().toString());
		dto.setProcessRequestId(executionHistory.getProcessRequest().getProcessRequestId().toString());
		dto.setExecutionDate(executionHistory.getExecutionDate());
		dto.setResult(executionHistory.getResult());
		dto.setStatus(executionHistory.getStatus());
		return dto;
	}
}
