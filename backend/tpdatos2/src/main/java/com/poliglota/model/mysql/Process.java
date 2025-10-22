import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "processes")
public class Process {
    @Id
    private String processId;
    private String name;
    private String description;
    private String processType;
    private double cost;

    // Getters and Setters
}
