package com.poliglota.DTO.request;

import lombok.Data;

@Data
public class ProcessRequestRequestDTO {
	private String userId;
	private String processId;
	private String status;
}
