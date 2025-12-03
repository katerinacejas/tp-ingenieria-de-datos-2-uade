package com.poliglota.service;

import com.poliglota.model.mysql.ExecutionHistory;
import com.poliglota.model.mysql.Process;
import com.poliglota.model.mysql.ProcessRequest;
import com.poliglota.repository.jpa.ExecutionHistoryRepository;
import com.poliglota.repository.jpa.ProcessRepository;
import com.poliglota.repository.jpa.ProcessRequestRepository;
import com.poliglota.DTO.ProcessDTO;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ProcessService {

    private final ProcessRepository processRepository;
	private final ProcessRequestRepository processRequestRepository;
	private final MongoTemplate mongoTemplate;
	private final ExecutionHistoryRepository executionHistoryRepository;

    public List<ProcessDTO> getAllProcesses() {
        return processRepository.findAll()
			.stream()
			.map(process -> this.toDto(process))
			.toList();
    }

    public ProcessDTO getProcessById(Long id) {
        Optional<Process> processOpt = processRepository.findById(id);
        return processOpt.map(this::toDto).orElse(null);
    }

    public Optional<ProcessDTO> getProcessByName(String name) {
        return Optional.ofNullable(toDto(processRepository.findByName(name)));
    }

    public List<ProcessDTO> getProcessesByType(String type) {
        return processRepository.findByProcessType(type)
			.stream()
			.map(process -> this.toDto(process))
			.toList();
    }

    public List<ProcessDTO> getProcessesByMaxCost(double maxCost) {
        return processRepository.findByCostLessThan(maxCost)
			.stream()
			.map(process -> this.toDto(process))
			.toList();		
    }

    public ProcessDTO saveProcess(ProcessDTO processDTO) {
        Process proceso = processRepository.save(toEntity(processDTO));
		return toDto(proceso);
    }

    public boolean deleteProcess(Long id) {
        if (processRepository.existsById(id)) {
            processRepository.deleteById(id);
            return true;
        }
        return false;
    }

	private Process toEntity(ProcessDTO dto) {
		Process process = new Process();
		process.setName(dto.getName());
		process.setDescription(dto.getDescription());
		process.setProcessType(dto.getProcessType());
		process.setCost(dto.getCost());                   
		return process;
	}
    
	private ProcessDTO toDto(Process process) {
		ProcessDTO dto = new ProcessDTO();
		dto.setName(process.getName());
		dto.setDescription(process.getDescription());
		dto.setProcessType(process.getProcessType());
		dto.setCost(process.getCost());
		dto.setId(process.getProcessId());
		return dto;
	}
}
