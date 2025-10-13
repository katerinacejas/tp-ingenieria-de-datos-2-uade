package com.project.repository.cassandra;

import com.project.model.cassandra.MaintenanceCheck;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MaintenanceCheckRepository extends CassandraRepository<MaintenanceCheck, String> {

    List<MaintenanceCheck> findBySensorId(String sensorId);
}
