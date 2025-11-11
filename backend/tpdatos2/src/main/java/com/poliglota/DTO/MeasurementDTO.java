package com.poliglota.DTO;

import java.time.Instant;
import lombok.Data;

@Data
public class MeasurementDTO {
	public String sensorId;
    public Double temperature;
    public Double humidity;
	public Instant timestamp;
}
