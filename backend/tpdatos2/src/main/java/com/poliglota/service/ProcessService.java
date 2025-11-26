package com.poliglota.service;

import com.poliglota.model.mysql.Process;
import com.poliglota.model.mysql.ProcessRequest;
import com.poliglota.repository.jpa.ProcessRepository;
import com.poliglota.repository.jpa.ProcessRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.poliglota.DTO.ProcessDTO;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProcessService {

    private final ProcessRepository processRepository;
	private final ProcessRequestRepository processRequestRepository;


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
		ProcessRequest solicitud = processRequestRepository.findByName(processDTO.getName());
        Process proceso = processRepository.save(toEntity(processDTO));
		solicitud.setProcess(proceso);
		processRequestRepository.save(solicitud);
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
		return dto;
	}
}
