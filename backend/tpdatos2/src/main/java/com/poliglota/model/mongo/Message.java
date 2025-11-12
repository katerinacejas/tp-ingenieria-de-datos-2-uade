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
    private Long messageId;
    private Long senderId;
    private Long recipientId; // puede ser ID de usuario o de grupo
    private LocalDateTime timestamp = LocalDateTime.now();
    private String content;
    private String recipienttype;  // "user" o "group"
}
