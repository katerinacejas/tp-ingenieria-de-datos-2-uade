package com.poliglota.DTO;

import java.time.LocalDateTime;
import lombok.*;

@Data
public class AlertsDTO {
	private String alertId;
	private String type;
	private String state;
	private String sensorId;
	private LocalDateTime datetime;
	private String descripction;
}
