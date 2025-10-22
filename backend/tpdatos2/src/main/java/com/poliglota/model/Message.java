import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "messages")
public class Message {
    @Id
    private String messageId;
    private String senderId;
    private String recipientId; // user or group
    private LocalDateTime timestamp;
    private String content;
    private String type; // private / group

    // Getters and Setters
}

