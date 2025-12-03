package com.poliglota.service;

import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

import com.poliglota.DTO.AggregatedResult;
import com.poliglota.DTO.ExecutionHistoryDTO;
import com.poliglota.DTO.ProcessRequestDTO;
import com.poliglota.model.mysql.ProcessRequest;
import com.poliglota.repository.jpa.ExecutionHistoryRepository;
import com.poliglota.repository.jpa.InvoiceRepository;
import com.poliglota.repository.jpa.ProcessRepository;
import com.poliglota.repository.jpa.ProcessRequestRepository;
import com.poliglota.repository.jpa.UserRepository;
import com.poliglota.model.mysql.User;
import com.poliglota.exception.UsuarioNotFoundException;
import com.poliglota.model.mysql.ExecutionHistory;
import com.poliglota.model.mysql.Invoice;
import com.poliglota.model.mysql.Process;
import com.poliglota.exception.ProcessNotFoundException;
import com.poliglota.exception.ProcessRequestNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcessRequestService {
	private final ProcessRequestRepository processRequestRepository;
	private final UserRepository userRepository;
	private final ProcessRepository processRepository;
	private final ExecutionHistoryRepository executionHistoryRepository;
	private final MeasurementService measurementService;
	private final InvoiceRepository invoiceRepository;

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
		processRequest.setCity(processRequestDTO.getCity());
		processRequest.setCountry(processRequestDTO.getCountry());
		processRequest.setStartDate(processRequestDTO.getStartDate());
		processRequest.setEndDate(processRequestDTO.getEndDate());
		processRequest.setAgrupacionDeDatos(processRequestDTO.getAgrupacionDeDatos());

		ProcessRequest savedRequest = processRequestRepository.save(processRequest);

		Invoice invoice = new Invoice();
        invoice.setUser(user);
        invoice.setIssueDate(LocalDateTime.now());
        invoice.setBilledProcessRequest(processRequest);
        invoice.setStatus("PENDIENTE");

		Invoice savedInvoice = invoiceRepository.save(invoice);

		savedRequest.setInvoice(savedInvoice);
    	savedRequest = processRequestRepository.save(savedRequest);

		return toDtoResponse(savedRequest);
	}

	public List<ProcessRequestDTO> getProcessRequestByUser(Long userId) {
		return processRequestRepository.findAllByUser_UserId(userId).stream()
				.map(processRequest -> toDtoResponse(processRequest))
				.toList();
	}

	public List<ProcessRequestDTO> getProcessRequestByUserAndStatus(Long userId, String status) {
		return processRequestRepository.findAllByUser_UserIdAndStatus(userId, status).stream()
				.map(this::toDtoResponse)
				.toList();
	}

	public List<ProcessRequestDTO> getProcessRequestByStatus(String status) {
		return processRequestRepository.findAllByStatus(status).stream()
				.map(this::toDtoResponse)
				.toList();
	}

	public ProcessRequestDTO getProcessRequestById(Long id) {
		return toDtoResponse(processRequestRepository.findById(id).orElse(null));
	}

	public ProcessRequestDTO updateStatusProcessRequest(ProcessRequestDTO processRequestRequestDTO) {
		ProcessRequest processRequest = processRequestRepository
				.findById(Long.parseLong(processRequestRequestDTO.getProcessRequestId()))
				.orElseThrow(() -> new ProcessRequestNotFoundException(
						"Process Request not found: " + processRequestRequestDTO.getProcessId()));

		processRequest.setStatus(processRequestRequestDTO.getStatus());

		return toDtoResponse(processRequestRepository.save(processRequest));
	}

	public List<ProcessRequestDTO> getAllProcessRequests() {
		return processRequestRepository.findAll()
				.stream()
				.map(processRequest -> this.toDtoResponse(processRequest))
				.toList();
	}

	public ExecutionHistoryDTO approveAndExecute(Long processRequestId) {
		ProcessRequest pr = processRequestRepository.findById(processRequestId)
				.orElseThrow(() -> new RuntimeException("Solicitud de proceso no encontrada: " + processRequestId));
		String resultText;
		try {
			resultText = ejecutarProceso(pr);
			ExecutionHistory eh = new ExecutionHistory();
			eh.setProcessRequest(pr);
			eh.setExecutionDate(LocalDateTime.now());
			eh.setResult(resultText);
			eh.setStatus("COMPLETADA");
			eh = executionHistoryRepository.save(eh);
			pr.setStatus("COMPLETADA");
			processRequestRepository.save(pr);
			
			return toExecutionHistoryDTO(eh);

		} catch (Exception e) {
			ExecutionHistory eh = new ExecutionHistory();
			eh.setProcessRequest(pr);
			eh.setExecutionDate(LocalDateTime.now());
			eh.setResult("Error al ejecutar el proceso: " + e.getMessage());
			eh.setStatus("FALLIDA");
			eh = executionHistoryRepository.save(eh);
			pr.setStatus("PENDIENTE");
			processRequestRepository.save(pr);

			return toExecutionHistoryDTO(eh);
		}
	}

	private String ejecutarProceso(ProcessRequest pr) {
		Process process = pr.getProcess();
		String metrica = process.getName().contains("HUMEDAD") ? "HUMEDAD" : "TEMPERATURA";
		String tipoProceso = process.getProcessType();
		String agrupacion = pr.getAgrupacionDeDatos();
		String ciudad = pr.getCity();
		String pais = pr.getCountry();

		List<AggregatedResult> resultados = measurementService.aggregate(
				metrica,
				tipoProceso,
				agrupacion,
				ciudad,
				pais,
				pr.getStartDate(),
				pr.getEndDate());

		return contruirTablita(process, pr, metrica, tipoProceso, agrupacion, resultados);
	}

	private String contruirTablita(
			Process process,
			ProcessRequest pr,
			String metrica,
			String tipoProceso,
			String agrupacion,
			List<AggregatedResult> resultados) {
		StringBuilder sb = new StringBuilder();

		sb.append("INFORME: ").append(process.getName()).append("\n");
		sb.append(process.getDescription()).append("\n\n");

		sb.append("Ciudad: ").append(pr.getCity() != null ? pr.getCity() : "-")
				.append("  |  País: ").append(pr.getCountry() != null ? pr.getCountry() : "-").append("\n");
		sb.append("Rango de fechas: ")
				.append(pr.getStartDate()).append(" a ").append(pr.getEndDate()).append("\n");
		sb.append("Agrupación de datos: ").append(agrupacion).append("\n");
		sb.append("Métrica: ").append(metrica).append("  |  Operación: ").append(tipoProceso).append("\n\n");

		String colPeriodo = agrupacion.equalsIgnoreCase("ANUAL") ? "Año" : "Periodo";
		String colValor = metrica.equalsIgnoreCase("HUMEDAD") ? "Humedad" : "Temperatura";

		sb.append(String.format("%-12s | %-15s\n", colPeriodo, colValor + " (" + tipoProceso + ")"));
		sb.append("-------------+-----------------\n");

		if (resultados == null || resultados.isEmpty()) {
			sb.append("Sin datos para los parámetros indicados.\n");
			return sb.toString();
		}

		for (AggregatedResult r : resultados) {
			sb.append(String.format("%-12s | %-15.2f\n", r.getPeriod(), r.getValue()));
		}

		return sb.toString();
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

	private ExecutionHistoryDTO toExecutionHistoryDTO(ExecutionHistory executionHistory) {
		ExecutionHistoryDTO dto = new ExecutionHistoryDTO();
		dto.setExecutionId(executionHistory.getExecutionId().toString());
		dto.setProcessRequestId(
				executionHistory.getProcessRequest().getProcessRequestId().toString());
		dto.setExecutionDate(executionHistory.getExecutionDate());
		dto.setResult(executionHistory.getResult());
		dto.setStatus(executionHistory.getStatus());
		return dto;
	}
}
