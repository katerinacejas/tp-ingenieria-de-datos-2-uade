package com.poliglota.repository;

import com.poliglota.model.mysql.Process;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProcessRepository extends JpaRepository<Process, Long> {
    
    // Buscar por nombre exacto
    Process findByName(String name);

    // Buscar por tipo de proceso
    List<Process> findByProcessType(String processType);

    // Buscar procesos con costo menor a cierto valor
    List<Process> findByCostLessThan(double maxCost);

}
