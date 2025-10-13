import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "roles")
public class Role {
    @Id
    private String roleId;
    private String description; // user / technician / admin

    // Getters and Setters
}