package com.poliglota.repository.cassandra;

import com.poliglota.model.cassandra.Alerts;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertRepository extends CassandraRepository<Alerts, String> {

    List<Alerts> findByStatus(String status);
}

