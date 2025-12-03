package com.poliglota.repository.mongo;

import com.poliglota.model.mongo.Alerts;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlertsRepository extends MongoRepository<Alerts, String> {

    List<Alerts> findByType(String type);

    List<Alerts> findByState(String state);

    List<Alerts> findBySensorId(String sensorId);

    List<Alerts> findByDatetimeBetween(LocalDateTime start, LocalDateTime end);
}
