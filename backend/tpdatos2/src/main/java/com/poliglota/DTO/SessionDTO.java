package com.poliglota.DTO;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SessionDTO {
	private String sessionId;
	private String userId;
	private String rolId;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private String status;

}
