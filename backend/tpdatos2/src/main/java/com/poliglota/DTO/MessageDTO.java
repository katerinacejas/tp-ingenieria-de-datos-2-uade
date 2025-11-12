package com.poliglota.DTO;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class MessageDTO {
	private String id;
    private String senderId; 
    private String recipientId; 
    private LocalDateTime timestamp;
    private String content;
    private String type;
}
