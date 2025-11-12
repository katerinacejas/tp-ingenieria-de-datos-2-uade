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
		processRequest.setInvoice(null);

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
		dto.setExecutionId(executionHistory.getExecutionId() != null ? executionHistory.getExecutionId().toString() : null);
		dto.setProcessRequestId(executionHistory.getProcessRequest() != null && executionHistory.getProcessRequest().getProcessRequestId() != null
			? executionHistory.getProcessRequest().getProcessRequestId().toString()
			: null);
		dto.setExecutionDate(executionHistory.getExecutionDate());
		dto.setResult(executionHistory.getResult());
		dto.setStatus(executionHistory.getStatus());
		return dto;
	}

	private ProcessRequestDTO toDtoResponse(ProcessRequest processRequest) {
		ProcessRequestDTO dto = new ProcessRequestDTO();
		dto.setRequestId(processRequest.getProcessRequestId() != null ? processRequest.getProcessRequestId().toString() : null);
		dto.setUserId(processRequest.getUser().getUserId().toString());
		dto.setProcessId(processRequest.getProcess().getProcessId().toString());
		dto.setRequestDate(processRequest.getRequestDate());
		dto.setStatus(processRequest.getStatus());
		if (processRequest.getInvoice() != null) {
			dto.setInvoiceId(processRequest.getInvoice().getInvoiceId().toString());
		} else {
			dto.setInvoiceId(null);
		}
		return dto;
	}

}
