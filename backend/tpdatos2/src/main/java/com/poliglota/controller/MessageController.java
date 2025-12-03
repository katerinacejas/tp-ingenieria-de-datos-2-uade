package com.poliglota.controller;

import com.poliglota.DTO.MessageDTO;
import com.poliglota.DTO.request.SendDirectRequestDTO;
import com.poliglota.DTO.request.SendGroupRequestDTO;
import com.poliglota.DTO.response.UsuarioResponseDTO;
import com.poliglota.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.poliglota.service.UsuarioService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

	private final MessageService messageService;
	private final UsuarioService usuarioService;

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

	@GetMapping("/direct/{userA}/{userB}")
	public List<MessageDTO> getDirect(  Long userA,   Long userB) {
		return messageService.getDirectConversation(userA, userB);
	}

	@GetMapping("/group/{groupId}")
	public List<MessageDTO> getGroupMessages(  String groupId) {
		return messageService.getGroupMessages(groupId);
	}

	@GetMapping("/direct/{userA}/conexiones")
	public List<String> getUsersMensajes( String user) {
		List <UsuarioResponseDTO> todosUsuariosDTO = usuarioService.getTodosLosUsuarios();
		List<String> usuariosInteractuaron = new ArrayList<>();
		for (UsuarioResponseDTO usuarioDTO : todosUsuariosDTO) {
			if (getDirect(Long.parseLong(user), usuarioDTO.getUserId()) != null){
				usuariosInteractuaron.add(usuarioDTO.getUserId().toString());
			}
		}
		return usuariosInteractuaron;
	}

}
