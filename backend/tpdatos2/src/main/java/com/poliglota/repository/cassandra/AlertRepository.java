package com.project.repository.cassandra;

import com.project.model.cassandra.Alert;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertRepository extends CassandraRepository<Alert, String> {

    List<Alert> findByStatus(String status);
}

