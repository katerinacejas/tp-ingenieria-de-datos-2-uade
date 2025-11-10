package com.poliglota.model.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "execution_history")
public class ExecutionHistory {
	@Id
	private String executionId;
	private String requestId;
	private LocalDateTime executionDate;
	private String result;
	private String status;

	// Getters and Setters
}
