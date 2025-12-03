package com.poliglota.repository.mongo;

import com.poliglota.model.mongo.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {

    @Query(value = "{ 'type': 'user', $or: [ " +
            "{ 'senderId': ?0, 'recipientId': ?1 }, " +
            "{ 'senderId': ?2, 'recipientId': ?3 } " +
            "] }")
    List<Message> findDirectConversation(Long userA, String userBStr, Long userB, String userAStr, Sort sort);

    @Query(value = "{ 'type': 'group', 'recipientId': ?0 }")
    List<Message> findByGroupId(String groupId, Sort sort);

    Page<Message> findBySenderId(Long senderId, Pageable pageable);
}
