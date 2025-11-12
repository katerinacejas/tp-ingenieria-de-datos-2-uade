package com.poliglota.DTO;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class InvoiceDTO {
	private String invoiceId;
	private String userId;
	private LocalDateTime issueDate;
	private List<String> billedProcessesIds;
	private String status;
}
