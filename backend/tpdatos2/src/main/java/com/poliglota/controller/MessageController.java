package com.poliglota.controller.mongo;

import com.poliglota.model.mongo.Message;
import com.poliglota.service.mongo.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    // 🔹 Obtener todos los mensajes
    @GetMapping
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    // 🔹 Obtener mensaje por ID
    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Long id) {
        return messageService.getMessageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Obtener mensajes enviados por un usuario
    @GetMapping("/sent/{senderId}")
    public ResponseEntity<List<Message>> getMessagesBySender(@PathVariable Long senderId) {
        return ResponseEntity.ok(messageService.getMessagesBySender(senderId));
    }

    // 🔹 Obtener mensajes recibidos por un usuario
    @GetMapping("/received/{recipientId}")
    public ResponseEntity<List<Message>> getMessagesByRecipient(@PathVariable Long recipientId) {
        return ResponseEntity.ok(messageService.getMessagesByRecipient(recipientId));
    }

    // 🔹 Obtener conversación entre dos usuarios
    @GetMapping("/conversation")
    public ResponseEntity<List<Message>> getConversation(
            @RequestParam Long senderId,
            @RequestParam Long recipientId) {
        return ResponseEntity.ok(messageService.getConversation(senderId, recipientId));
    }

    // 🔹 Crear o actualizar un mensaje
    @PostMapping
    public ResponseEntity<Message> saveMessage(@RequestBody Message message) {
        return ResponseEntity.ok(messageService.saveMessage(message));
    }

    // 🔹 Eliminar mensaje
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}
