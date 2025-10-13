import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "process_requests")
public class ProcessRequest {
    @Id
    private String requestId;
    private String userId;
    private String processId;
    private LocalDateTime requestDate;
    private String status; // pending / completed

    // Getters and Setters
}
