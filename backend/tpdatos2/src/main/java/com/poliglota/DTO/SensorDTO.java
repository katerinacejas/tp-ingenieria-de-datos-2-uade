package com.poliglota.DTO;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SensorDTO {
	private String Id;
	private String name;
	private String type;
	private double latitud;
	private double longitud;
	private String city;
	private String country;
	private String estado;
	private LocalDateTime startDate;
}
