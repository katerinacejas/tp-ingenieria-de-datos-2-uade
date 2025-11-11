package com.poliglota.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.poliglota.model.mongo.Sensor;
import java.util.List;

public interface SensorRepository extends MongoRepository<Sensor, String> {
	List<Sensor> findByActive(boolean active);
}
