package com.poliglota.controller;

import com.poliglota.DTO.response.UsuarioResponseDTO;
import com.poliglota.service.UsuarioService;
import com.poliglota.exception.UsuarioNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("/me")
	@PreAuthorize("hasAnyRole('USUARIO', 'MANTENIMIENTO', 'ADMIN')")
	public UsuarioResponseDTO getPerfilUsuarioAutenticado(Authentication auth) {
		String email = auth.getName();
		return usuarioService.getUsuarioPorMail(email)
				.orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con email: " + email));
	}

	@GetMapping
	public List<UsuarioResponseDTO> getTodosLosUsuarios() {
		return usuarioService.getTodosLosUsuarios();
	}

	public List<UsuarioResponseDTO> getUsuariosYMantenimiento() {
		return usuarioService.getUsuariosYMantenimiento();
	}

	@GetMapping("/buscar")
	@PreAuthorize("hasRole('ADMIN')")
	public UsuarioResponseDTO getUsuarioPorMail(   String mail) {
		return usuarioService.getUsuarioPorMail(mail)
				.orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con email: " + mail));
	}

	public UsuarioResponseDTO getUsuarioPorId(Long id) {
		return usuarioService.getUsuarioPorId(id)
				.orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con id: " + id));
	}

}