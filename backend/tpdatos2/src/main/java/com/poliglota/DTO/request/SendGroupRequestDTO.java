package com.poliglota.DTO.request;

import lombok.Data;

@Data
public class SendGroupRequestDTO {
	private Long senderId;
	private String groupId;
	private String content;
}
