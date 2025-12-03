package com.poliglota.repository.mongo;

import com.poliglota.model.mongo.Sensor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorRepository extends MongoRepository<Sensor, String> {

	List<Sensor> findByType(String type);

	List<Sensor> findByEstado(String estado);

	List<Sensor> findByCountry(String country);

	List<Sensor> findByCity(String city);

	List<Sensor> findByNameIgnoreCase(String name);

	List<Sensor> findByCityAndCountry(String city, String country);
}
