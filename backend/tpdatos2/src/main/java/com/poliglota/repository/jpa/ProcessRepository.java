package com.poliglota.repository.jpa;

import com.poliglota.model.mysql.Process;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProcessRepository extends JpaRepository<Process, Long> {
    
    Process findByName(String name);

	boolean existsByName(String name);

    List<Process> findByProcessType(String processType);

    List<Process> findByCostLessThan(double maxCost);

}
