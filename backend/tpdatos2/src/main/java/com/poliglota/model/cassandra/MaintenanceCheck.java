import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import java.time.LocalDateTime;

@Table("maintenance_checks")
public class MaintenanceCheck {
    @PrimaryKey
    private String checkId;
    private String sensorId;
    private LocalDateTime reviewDate;
    private String sensorStatus;
    private String notes;

    
}
