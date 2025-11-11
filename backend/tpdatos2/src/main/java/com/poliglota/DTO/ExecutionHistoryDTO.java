package com.poliglota.DTO;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ExecutionHistoryDTO {
	private String executionId;
	private String processRequestId;
	private LocalDateTime executionDate;
	private String result;
	private String status;
}
