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
    private String id;
    private Long senderId; // ID del usuario que envía el mensaje
    private String recipientId; // puede ser ID de usuario o de grupo
    private LocalDateTime timestamp;
    private String content;
    private String type;  // "user" o "group"
}
