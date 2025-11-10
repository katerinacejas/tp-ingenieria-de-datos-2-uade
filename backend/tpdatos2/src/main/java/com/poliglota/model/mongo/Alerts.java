package com.poliglota.model.mongo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "alerts")
public class Alerts {
	@Id
    private String alertId;

    private String type; // sensor / climática
	private String state;
    private String sensorId; 
    private LocalDateTime datetime;
    private String descripction;


    
}
