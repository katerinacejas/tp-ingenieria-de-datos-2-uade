import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "sessions")
public class Session {
    @Id
    private String sessionId;
    private String userId;
    private String roleId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status; // active / inactive

    // Getters and Setters
}
