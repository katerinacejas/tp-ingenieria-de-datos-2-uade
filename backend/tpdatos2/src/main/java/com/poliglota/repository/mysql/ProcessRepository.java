package com.poliglota.repository.mysql;

import com.poliglota.model.mysql.Process;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessRepository extends JpaRepository<Process, Long> {

    
    // Buscar por nombre exacto
    Process findByName(String name);

    // Buscar por tipo de proceso
    java.util.List<Process> findByProcessType(String processType);

    // Buscar procesos con costo menor a cierto valor
    java.util.List<Process> findByCostLessThan(double maxCost);
}
