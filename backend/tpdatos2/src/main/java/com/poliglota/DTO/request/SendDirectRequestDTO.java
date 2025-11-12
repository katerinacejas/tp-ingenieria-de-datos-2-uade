package com.poliglota.DTO.request;

import lombok.Data;

@Data
public class SendDirectRequestDTO {
	private Long senderId;
	private Long recipientUserId;
	private String content;

}
