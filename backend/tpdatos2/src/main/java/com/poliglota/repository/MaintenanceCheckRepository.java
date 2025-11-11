package com.poliglota.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.poliglota.model.mongo.MaintenanceCheck;

import java.util.List;

@Repository
public interface MaintenanceCheckRepository extends CassandraRepository<MaintenanceCheck, String> {

    List<MaintenanceCheck> findBySensorId(String sensorId);
}
