package com.poliglota.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poliglota.model.mongo.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
	List<Message> findByRecipientId(Long recipientId);

	List<Message> findBySenderId(Long senderId);

	List<Message> findByType(String type);
}
