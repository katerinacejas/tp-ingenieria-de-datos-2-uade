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
	private double latitud;
	private double longitud;
	private String city;
	private String country;
	private String estado;
	private LocalDateTime startDate;

	public String getEstado(){
		return this.estado;
	}
}
