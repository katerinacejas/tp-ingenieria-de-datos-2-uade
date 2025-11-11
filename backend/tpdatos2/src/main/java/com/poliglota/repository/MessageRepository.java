
import com.poliglota.model.mongo.Sensor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
	List<Message> findByRecipientId(Long recipientId);

	List<Message> findBySenderId(Long senderId);

	List<Message> findBySenderIdAndRecipientId(Long senderId, Long recipientId);

	List<Message> findByType(String type);
}
