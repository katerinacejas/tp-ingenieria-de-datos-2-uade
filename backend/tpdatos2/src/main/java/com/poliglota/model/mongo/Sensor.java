package com.poliglota.model.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;
import java.time.LocalDateTime;

@Document(collection = "sensors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sensor {
	@Id
	private String id;
	private String name;
	private String type;
	private String city;
	private String country;
	private boolean active;
	private LocalDateTime startDate;
}
