package com.poliglota.service;

import com.poliglota.model.mongo.Group;
import com.poliglota.model.mongo.Message;
import com.poliglota.repository.GroupRepository;
import com.poliglota.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final GroupRepository groupRepository;

    //  Obtener todos los mensajes
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    //  Obtener mensaje por ID
    public Optional<Message> getMessageById(String id) {
        return messageRepository.findById(Long.parseLong(id));
    }

    //  Obtener mensajes enviados por usuario
    public List<Message> getMessagesBySender(Long senderId) {
        return messageRepository.findBySenderId(senderId);
    }

    //  Obtener mensajes recibidos (privados)
    public List<Message> getMessagesByRecipient(Long recipientId) {
        return messageRepository.findByRecipientIdAndRecipientType(recipientId, "user");
    }

    //  Obtener conversación entre dos usuarios
    public List<Message> getConversation(Long senderId, Long recipientId) {
        return messageRepository.findConversationBetweenUsers(senderId, recipientId);
    }

    //  Obtener mensajes de grupo
    public List<Message> getMessagesByGroup(Long groupId) {
        return messageRepository.findByRecipientTypeAndRecipientId("group", groupId);
    }

    //  Enviar mensaje
    public Message sendMessage(Long senderId, Long recipientId, String recipientType, String content) {
        if ("group".equalsIgnoreCase(recipientType)) {
            groupRepository.findById(recipientId)
                    .orElseThrow(() -> new IllegalArgumentException("Grupo no encontrado con ID: " + recipientId));
        }

        Message message = new Message();
        message.setSenderId(senderId);
        message.setRecipientId(recipientId);
        message.setRecipientType(recipientType);
        message.setContent(content);

        return messageRepository.save(message);
    }

    //  Guardar o actualizar mensaje (manual)
    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

}
