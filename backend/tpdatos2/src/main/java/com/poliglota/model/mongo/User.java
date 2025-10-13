package com.poliglota.model.mongo;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;
import java.time.LocalDateTime;

@Document(collection = "users")
@Data @NoArgsConstructor @AllArgsConstructor
public class User {
    @Id private String id;
    private String fullName;
    private String email;
    private String password;
    private boolean active = true;
    private LocalDateTime registeredAt = LocalDateTime.now();
}
