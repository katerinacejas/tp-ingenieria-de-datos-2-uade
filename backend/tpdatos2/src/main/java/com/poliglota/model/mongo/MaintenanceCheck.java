package com.poliglota.model.mongo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "maintenance_checks")
public class MaintenanceCheck {
	@Id
	private String checkId;
	private String sensorId;
	private LocalDateTime reviewDate;
	private String sensorStatus;
	private String notes;

}
