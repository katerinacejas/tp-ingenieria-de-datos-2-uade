package com.poliglota.service;

import com.poliglota.model.mysql.Message;
import com.poliglota.repository.mysql.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    /**
     * Envía un mensaje entre dos usuarios (privado o grupal).
     */
    public Message sendMessage(Long senderId, Long recipientId, String content, String type) {
        Message message = new Message();
        message.setSenderId(senderId);
        message.setRecipientId(recipientId);
        message.setContent(content);
        message.setType(type);
        message.setTimestamp(LocalDateTime.now());
        return messageRepository.save(message);
    }

    /**
     * Obtiene los mensajes enviados por un usuario.
     */
    public List<Message> getMessagesSentByUser(Long senderId) {
        return messageRepository.findBySenderId(senderId);
    }

    /**
     * Obtiene los mensajes recibidos por un usuario.
     */
    public List<Message> getMessagesReceivedByUser(Long recipientId) {
        return messageRepository.findByRecipientId(recipientId);
    }

    /**
     * Obtiene los mensajes de tipo específico (por ejemplo: private, group).
     */
    public List<Message> getMessagesByType(String type) {
        return messageRepository.findByType(type);
    }

    /**
     * Elimina un mensaje por su ID.
     */
    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }
}
