import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "groups")
public class Group {
    @Id
    private String groupId;
    private String name;
    private List<String> memberIds;

    // Getters and Setters
}
