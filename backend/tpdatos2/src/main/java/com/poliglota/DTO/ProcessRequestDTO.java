package com.poliglota.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ProcessRequestDTO {
	private String processRequestId;
	private String userId;
	private String processId;
	private String status;
	private LocalDateTime requestDate;
	private String invoiceId;
	private String city;   
	private String country;  
	private LocalDate startDate;
	private LocalDate endDate;
	private String agrupacionDeDatos;
}
