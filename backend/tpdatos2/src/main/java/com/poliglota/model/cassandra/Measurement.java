package com.poliglota.model.cassandra;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.*;

@Table("measurements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Measurement {
	@PrimaryKey
	private MeasurementKey key;
	private Double temperature;
	private Double humidity;
}
