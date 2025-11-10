package com.poliglota.repository.cassandra;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import com.poliglota.model.cassandra.Measurement;
import com.poliglota.model.cassandra.MeasurementKey;
import java.util.List;

@Repository
public interface MeasurementRepository extends CassandraRepository<Measurement, MeasurementKey> {
	List<Measurement> findByKeySensorId(String sensorId);
}
