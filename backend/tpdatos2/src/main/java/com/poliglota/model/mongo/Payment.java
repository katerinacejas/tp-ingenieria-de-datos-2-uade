import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "payments")
public class Payment {
    @Id
    private String paymentId;
    private String invoiceId;
    private LocalDateTime paymentDate;
    private double amount;
    private String paymentMethod;

    // Getters and Setters
}
