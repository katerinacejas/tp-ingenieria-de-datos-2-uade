package com.poliglota.model.cassandra;

import lombok.*;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.mapping.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

@PrimaryKeyClass
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeasurementKey implements Serializable {

	@PrimaryKeyColumn(name = "sensor_id", type = PrimaryKeyType.PARTITIONED)
	private String sensorId;
	
	@PrimaryKeyColumn(name = "timestamp", type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
	private LocalDate timestamp;
}
