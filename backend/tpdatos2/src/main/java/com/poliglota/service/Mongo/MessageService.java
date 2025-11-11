package com.poliglota.service.mongo;

import com.poliglota.model.mongo.Message;
import com.poliglota.repository.mongo.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    // 🔹 Obtener todos los mensajes
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    // 🔹 Buscar mensaje por ID
    public Optional<Message> getMessageById(Long id) {
        return messageRepository.findById(id);
    }

    // 🔹 Buscar mensajes enviados por un usuario
    public List<Message> getMessagesBySender(Long senderId) {
        return messageRepository.findBySenderId(senderId);
    }

    // 🔹 Buscar mensajes recibidos por un usuario
    public List<Message> getMessagesByRecipient(Long recipientId) {
        return messageRepository.findByRecipientId(recipientId);
    }

    // 🔹 Buscar conversación entre dos usuarios
    public List<Message> getConversation(Long senderId, Long recipientId) {
        return messageRepository.findBySenderIdAndRecipientId(senderId, recipientId);
    }

    // 🔹 Crear o actualizar un mensaje
    public Message saveMessage(Message message) {
        if (message.getTimestamp() == null) {
            message.setTimestamp(LocalDateTime.now());
        }
        return messageRepository.save(message);
    }

    // 🔹 Eliminar mensaje por ID
    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }
}
