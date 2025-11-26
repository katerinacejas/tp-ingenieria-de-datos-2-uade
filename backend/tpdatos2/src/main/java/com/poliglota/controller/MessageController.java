package com.poliglota.controller;

import com.poliglota.DTO.MessageDTO;
import com.poliglota.DTO.request.SendDirectRequestDTO;
import com.poliglota.DTO.request.SendGroupRequestDTO;
import com.poliglota.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

	private final MessageService messageService;

	// Enviar mensaje directo A -> B
	@PostMapping("/direct")
	public MessageDTO sendDirect(  SendDirectRequestDTO req) {
		try {
			return messageService.sendDirectMessage(req.getSenderId(), req.getRecipientUserId(), req.getContent());
		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (IllegalStateException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		}
	}

	// Enviar mensaje a un grupo
	@PostMapping("/group")
	public MessageDTO sendToGroup(  SendGroupRequestDTO req) {
		try {
			return messageService.sendGroupMessage(req.getSenderId(), req.getGroupId(), req.getContent());
		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (IllegalStateException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		}
	}

	// Obtener toda la conversación directa (A <-> B) ordenada por timestamp asc
	@GetMapping("/direct/{userA}/{userB}")
	public List<MessageDTO> getDirect(  Long userA,   Long userB) {
		return messageService.getDirectConversation(userA, userB);
	}

	// Mensajes de un grupo
	@GetMapping("/group/{groupId}")
	public List<MessageDTO> getGroupMessages(  String groupId) {
		return messageService.getGroupMessages(groupId);
	}

}
