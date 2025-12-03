package com.poliglota.DTO;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class InvoiceDTO {
	private String invoiceId;
	private String userId;
	private LocalDateTime issueDate;
	private String billedProcessRequest;
	private String status;
}
