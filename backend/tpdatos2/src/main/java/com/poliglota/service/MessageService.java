package com.poliglota.service;

import com.poliglota.model.mongo.Group;
import com.poliglota.model.mongo.Message;
import com.poliglota.repository.GroupRepository;
import com.poliglota.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import com.poliglota.DTO.MessageDTO;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final GroupRepository groupRepository;

    public MessageDTO sendDirectMessage(Long senderId, Long recipientUserId, String content) {
        validateContent(content);

        Message msg = new Message();
        msg.setSenderId(senderId);
        msg.setRecipientId(recipientUserId.toString());
        msg.setType("user");
        msg.setTimestamp(LocalDateTime.now());
        msg.setContent(content.trim());

        return toDTO(messageRepository.save(msg));
    }

    public MessageDTO sendGroupMessage(Long senderId, String groupId, String content) {
        validateContent(content);

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Grupo no encontrado: " + groupId));

        // Validar pertenencia del remitente al grupo
        if (group.getMemberIds() == null || !group.getMemberIds().contains(senderId)) {
            throw new IllegalStateException("El usuario " + senderId + " no pertenece al grupo " + groupId);
        }

        Message msg = new Message();
        msg.setSenderId(senderId);
        msg.setRecipientId(groupId);
        msg.setType("group");
        msg.setTimestamp(LocalDateTime.now());
        msg.setContent(content.trim());

        return toDTO(messageRepository.save(msg));
    }

    public List<MessageDTO> getDirectConversation(Long userA, Long userB) {
        List<Message> messages = messageRepository.findDirectConversation(
                userA, userB.toString(), userB, userA.toString(),
                Sort.by(Sort.Direction.ASC, "timestamp")
        );
        return messages.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<MessageDTO> getGroupMessages(String groupId) {
        List<Message> messages = messageRepository.findByGroupId(
                groupId,
                Sort.by(Sort.Direction.ASC, "timestamp")
        );
        return messages.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private void validateContent(String content) {
        if (!StringUtils.hasText(content)) {
            throw new IllegalArgumentException("El contenido del mensaje no puede estar vacío.");
        }
    }

    private MessageDTO toDTO(Message m) {
        MessageDTO dto = new MessageDTO();
        dto.setId(m.getId());
        dto.setSenderId(m.getSenderId().toString());
        dto.setRecipientId(m.getRecipientId());
        dto.setTimestamp(m.getTimestamp());
        dto.setContent(m.getContent());
        dto.setType(m.getType());
        return dto;
    }
}
