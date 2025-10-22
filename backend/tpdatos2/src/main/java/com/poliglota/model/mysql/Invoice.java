import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "invoices")
public class Invoice {
    @Id
    private String invoiceId;
    private String userId;
    private LocalDateTime issueDate;
    private List<String> billedProcesses;
    private String status; // pending / paid / overdue

    // Getters and Setters
}
