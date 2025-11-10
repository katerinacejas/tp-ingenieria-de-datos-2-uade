package com.poliglota.service;

import com.poliglota.model.mysql.Process;
import com.poliglota.repository.ProcessRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProcessService {

    private final ProcessRepository processRepository;

    // 🔹 Obtener todos los procesos
    public List<Process> getAllProcesses() {
        return processRepository.findAll();
    }

    // 🔹 Buscar proceso por ID
    public Optional<Process> getProcessById(Long id) {
        return processRepository.findById(id);
    }

    // 🔹 Buscar proceso por nombre exacto
    public Optional<Process> getProcessByName(String name) {
        return Optional.ofNullable(processRepository.findByName(name));
    }

    // 🔹 Buscar procesos por tipo
    public List<Process> getProcessesByType(String type) {
        return processRepository.findByProcessType(type);
    }

    // 🔹 Buscar procesos con costo menor a un valor
    public List<Process> getProcessesByMaxCost(double maxCost) {
        return processRepository.findByCostLessThan(maxCost);
    }

    // 🔹 Guardar (crear o actualizar) un proceso
    public Process saveProcess(Process process) {
        return processRepository.save(process);
    }

    // 🔹 Eliminar un proceso por ID
    public boolean deleteProcess(Long id) {
        if (processRepository.existsById(id)) {
            processRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
}
