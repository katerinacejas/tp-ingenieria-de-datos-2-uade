package com.poliglota.repository.mongo;

import com.poliglota.model.mongo.Sensor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorRepository extends MongoRepository<Sensor, String> {

    //  Buscar sensores por tipo (ej: temperatura, humedad)
    List<Sensor> findByType(String type);

    //  Buscar sensores activos o inactivos
    List<Sensor> findByEstado(String estado);

    //  Buscar sensores por país
    List<Sensor> findByCountry(String country);

    //  Buscar sensores por ciudad
    List<Sensor> findByCity(String city);

    //  Buscar sensores por nombre (ignora mayúsculas/minúsculas)
    List<Sensor> findByNameIgnoreCase(String name);
}
