package com.poliglota.service;

import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

import com.poliglota.DTO.ProcessRequestDTO;
import com.poliglota.model.mysql.ProcessRequest;
import com.poliglota.repository.jpa.ExecutionHistoryRepository;
import com.poliglota.repository.jpa.ProcessRepository;
import com.poliglota.repository.jpa.ProcessRequestRepository;
import com.poliglota.repository.jpa.UserRepository;
import com.poliglota.model.mysql.User;
import com.poliglota.exception.UsuarioNotFoundException;
import com.poliglota.model.mysql.Process;
import com.poliglota.exception.ProcessNotFoundException;
import com.poliglota.exception.ProcessRequestNotFoundException;
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

	public ProcessRequestDTO createProcessRequest(ProcessRequestDTO processRequestDTO) {
		User user = userRepository.findById(Long.parseLong(processRequestDTO.getUserId()))
				.orElseThrow(() -> new UsuarioNotFoundException("user not found: " + processRequestDTO.getUserId()));

		Process process = processRepository.findById(Long.parseLong(processRequestDTO.getProcessId()))
				.orElseThrow(
						() -> new ProcessNotFoundException("Process not found: " + processRequestDTO.getProcessId()));

		ProcessRequest processRequest = new ProcessRequest();
		processRequest.setProcess(process);
		processRequest.setUser(user);
		processRequest.setRequestDate(LocalDateTime.now());
		processRequest.setStatus("PENDIENTE");
		processRequest.setInvoice(null);
		processRequest.setCity(processRequestDTO.getCity());
		processRequest.setCountry(processRequestDTO.getCountry());
		processRequest.setStartDate(processRequestDTO.getStartDate());
		processRequest.setEndDate(processRequestDTO.getEndDate());
		processRequest.setAgrupacionDeDatos(processRequestDTO.getAgrupacionDeDatos());

		return toDtoResponse(processRequestRepository.save(processRequest));
	}

	public List<ProcessRequestDTO> getProcessRequestByUser (Long userId) {
		return processRequestRepository.findAllByUser_UserId(userId).stream()
				.map(processRequest -> toDtoResponse(processRequest))
				.toList();
	}

	public ProcessRequestDTO updateStatusProcessRequest(ProcessRequestDTO processRequestRequestDTO) {
		ProcessRequest processRequest = processRequestRepository
				.findById(Long.parseLong(processRequestRequestDTO.getProcessRequestId()))
				.orElseThrow(() -> new ProcessRequestNotFoundException(
						"Process Request not found: " + processRequestRequestDTO.getProcessId()));

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
		return executionHistoryRepository.findByProcessRequest_ProcessRequestId(Long.parseLong(processRequestId))
				.stream()
				.map(executionHistory -> this.executionToDtoResponse(executionHistory))
				.toList();
	}

	private ExecutionHistoryDTO executionToDtoResponse(ExecutionHistory executionHistory) {
		ExecutionHistoryDTO dto = new ExecutionHistoryDTO();
		dto.setExecutionId(
				executionHistory.getExecutionId() != null ? executionHistory.getExecutionId().toString() : null);
		dto.setProcessRequestId(executionHistory.getProcessRequest() != null
				&& executionHistory.getProcessRequest().getProcessRequestId() != null
						? executionHistory.getProcessRequest().getProcessRequestId().toString()
						: null);
		dto.setExecutionDate(executionHistory.getExecutionDate());
		dto.setResult(executionHistory.getResult());
		dto.setStatus(executionHistory.getStatus());
		return dto;
	}

	private ProcessRequestDTO toDtoResponse(ProcessRequest processRequest) {
		ProcessRequestDTO dto = new ProcessRequestDTO();
		dto.setProcessRequestId(processRequest.getProcessRequestId().toString());
		dto.setUserId(processRequest.getUser().getUserId().toString());
		dto.setProcessId(processRequest.getProcess().getProcessId().toString());
		dto.setStatus(processRequest.getStatus());
		dto.setRequestDate(processRequest.getRequestDate());
		if (processRequest.getInvoice() != null) {
			dto.setInvoiceId(processRequest.getInvoice().getInvoiceId().toString());
		} else {
			dto.setInvoiceId(null);
		}
		dto.setCity(processRequest.getCity());
		dto.setCountry(processRequest.getCountry());
		dto.setStartDate(processRequest.getStartDate());
		dto.setEndDate(processRequest.getEndDate());
		dto.setAgrupacionDeDatos(processRequest.getAgrupacionDeDatos());
		return dto;
	}

}
