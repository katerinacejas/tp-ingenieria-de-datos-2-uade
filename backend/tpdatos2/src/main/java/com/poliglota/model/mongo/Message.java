package com.poliglota.model.mongo;

import lombok.*;
import java.time.LocalDateTime;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Document(collection = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    private Long id;
    private Long senderId;
    private Long recipientId;
    private LocalDateTime timestamp = LocalDateTime.now();
    private String content;
    private String type; 
}
