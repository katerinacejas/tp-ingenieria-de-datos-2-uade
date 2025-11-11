package com.poliglota.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.poliglota.model.mongo.Alerts;

import java.util.List;

@Repository
public interface AlertRepository extends CassandraRepository<Alerts, String> {

    List<Alerts> findByStatus(String status);
}

