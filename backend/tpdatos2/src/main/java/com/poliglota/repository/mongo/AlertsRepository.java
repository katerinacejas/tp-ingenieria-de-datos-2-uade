package com.poliglota.repository.mongo;

import com.poliglota.model.mongo.Alerts;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlertsRepository extends MongoRepository<Alerts, String> {

    //  Buscar alertas por tipo (ej: temperatura, sistema, seguridad)
    List<Alerts> findByType(String type);

    //  Buscar alertas por estado (ej: activa, resuelta)
    List<Alerts> findByState(String state);

    //  Buscar alertas de un sensor específico
    List<Alerts> findBySensorId(String sensorId);

    //  Buscar alertas entre fechas
    List<Alerts> findByDatetimeBetween(LocalDateTime start, LocalDateTime end);
}
