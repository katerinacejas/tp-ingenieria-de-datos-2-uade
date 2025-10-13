import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "accounts")
public class Account {
    @Id
    private String accountId;
    private String userId;
    private double currentBalance;
    private List<String> transactionHistory;

    // Getters and Setters
}
