package com.poliglota.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import com.poliglota.model.mongo.MaintenanceCheck;

import java.util.List;

@Repository
public interface MaintenanceCheckRepository extends MongoRepository<MaintenanceCheck, String> {

     List<MaintenanceCheck> findBySensorId(String sensorId);

    // Buscar revisiones dentro de un rango de fechas
    List<MaintenanceCheck> findByReviewDateBetween(LocalDateTime start, LocalDateTime end);

    // Buscar por estado del sensor
    List<MaintenanceCheck> findBySensorStatus(String sensorStatus);
}
