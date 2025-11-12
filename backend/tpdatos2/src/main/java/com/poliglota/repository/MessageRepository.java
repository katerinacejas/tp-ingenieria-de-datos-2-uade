package com.poliglota.repository;

import com.poliglota.model.mongo.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, Long> {

    List<Message> findBySenderId(Long senderId);

    List<Message> findByRecipientIdAndRecipientType(Long recipientId, String recipientType);

    List<Message> findByRecipientTypeAndRecipientId(String recipientType, Long groupId);

    // Conversación entre dos usuarios
    List<Message> findBySenderIdAndRecipientIdOrSenderIdAndRecipientId(
            Long sender1, Long recipient1, Long sender2, Long recipient2);

    default List<Message> findConversationBetweenUsers(Long userA, Long userB) {
        return findBySenderIdAndRecipientIdOrSenderIdAndRecipientId(userA, userB, userB, userA);
    }
}
