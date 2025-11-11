package com.poliglota.service;

import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

import com.poliglota.DTO.ProcessRequestDTO;
import com.poliglota.DTO.request.ProcessRequestRequestDTO;
import com.poliglota.model.mysql.ProcessRequest;
import com.poliglota.repository.ProcessRequestRepository;
import com.poliglota.repository.UserRepository;
import com.poliglota.model.mysql.User;
import com.poliglota.exception.UsuarioNotFoundException;
import com.poliglota.model.mysql.Process;
import com.poliglota.repository.ProcessRepository;
import com.poliglota.exception.ProcessNotFoundException;
import com.poliglota.exception.ProcessRequestNotFoundException;
import com.poliglota.repository.ExecutionHistoryRepository;
import com.poliglota.model.mysql.ExecutionHistory;
import com.poliglota.DTO.ExecutionHistoryDTO;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcessRequestService {
	private final ProcessRequestRepository processRequestRepository;
	private final UserRepository userRepository;
	private final ProcessRepository processRepository;
	private final ExecutionHistoryRepository executionHistoryRepository;

	public ProcessRequestDTO createProcessRequest(ProcessRequestRequestDTO processRequestRequestDTO) {
		User user = userRepository.findById(Long.parseLong(processRequestRequestDTO.getUserId()))
				.orElseThrow(() -> new UsuarioNotFoundException("user not found: " + processRequestRequestDTO.getUserId()));
        
        Process process = processRepository.findById(Long.parseLong(processRequestRequestDTO.getProcessId()))
                .orElseThrow(() -> new ProcessNotFoundException("Process not found: " + processRequestRequestDTO.getProcessId()));

        ProcessRequest processRequest = new ProcessRequest();
        processRequest.setProcess(process);
		processRequest.setUser(user);
        processRequest.setRequestDate(LocalDateTime.now());
		processRequest.setStatus("pendiente");

        return toDtoResponse(processRequestRepository.save(processRequest));
	}

	public ProcessRequestDTO updateStatusProcessRequest(ProcessRequestRequestDTO processRequestRequestDTO) {
		ProcessRequest processRequest = processRequestRepository.findById(processRequestRequestDTO.getProcessId())
				.orElseThrow(() -> new ProcessRequestNotFoundException("Process Request not found: " + processRequestRequestDTO.getProcessId()));

		if (processRequestRequestDTO.getStatus() == "pendiente") {
			processRequest.setStatus("completado");
		}

		return toDtoResponse(processRequestRepository.save(processRequest));
	}

	public List<ProcessRequestDTO> getAllProcessRequests() {
		return processRequestRepository.findAll()
			.stream()
			.map(processRequest -> this.toDtoResponse(processRequest))
			.toList();
	}

	public List<ExecutionHistoryDTO> getExecutionHistoryByProcessRequestId(String processRequestId) {
		return executionHistoryRepository.findByRequestId(processRequestId)
			.stream()
			.map(executionHistory -> this.executionToDtoResponse(executionHistory))
			.toList();
	}

	private ExecutionHistoryDTO executionToDtoResponse(ExecutionHistory executionHistory) {
		ExecutionHistoryDTO dto = new ExecutionHistoryDTO();
		dto.setExecutionId(executionHistory.getExecutionId());
		dto.setProcessRequestId(executionHistory.getProcessRequest().getRequestId());
		dto.setExecutionDate(executionHistory.getExecutionDate());
		dto.setResult(executionHistory.getResult());
		dto.setStatus(executionHistory.getStatus());
		return dto;
	}
	

	private ProcessRequestRequestDTO toDtoRequest(ProcessRequest processRequest) {
		ProcessRequestRequestDTO dto = new ProcessRequestRequestDTO();
		dto.setProcessId(processRequest.getUser().getId().toString());
		dto.setProcessId(processRequest.getProcess().getId().toString());
		dto.setStatus(processRequest.getStatus());
		return dto;
	}

	private ProcessRequestDTO toDtoResponse(ProcessRequest processRequest) {
		ProcessRequestDTO dto = new ProcessRequestDTO();
		dto.setRequestId(processRequest.getRequestId());
		dto.setUserId(processRequest.getUser().getId().toString());
		dto.setProcessId(processRequest.getProcess().getId().toString());
		dto.setRequestDate(processRequest.getRequestDate());
		dto.setStatus(processRequest.getStatus());
		return dto;
	}

}
