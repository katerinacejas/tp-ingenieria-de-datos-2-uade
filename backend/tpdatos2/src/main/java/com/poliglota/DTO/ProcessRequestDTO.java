package com.poliglota.DTO;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ProcessRequestDTO {
	private String requestId;
	private String userId;
	private String processId;
	private String status;
	private LocalDateTime requestDate;
	private String invoiceId;
}
