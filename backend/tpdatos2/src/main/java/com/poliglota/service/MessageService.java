package com.poliglota.service.mysql;

import com.poliglota.model.mysql.Message;
import com.poliglota.repository.mysql.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public List<Message> getMessagesForRecipient(Long recipientId) {
        return messageRepository.findByRecipientId(recipientId);
    }

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    public boolean deleteMessage(Long id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
