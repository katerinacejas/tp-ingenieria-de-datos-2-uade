import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import java.time.LocalDateTime;

@Table("alertas")
public class Alerts {
    @PrimaryKey
    private String alertId;
    private String type; // sensor / climática
    private String sensorId; // opcional
    private LocalDateTime datetime;
    private String descripction;
    private String state; // activa/resuelta

    
}
