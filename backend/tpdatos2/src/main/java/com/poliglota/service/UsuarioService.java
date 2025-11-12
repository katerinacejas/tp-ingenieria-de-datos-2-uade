package com.poliglota.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poliglota.repository.SessionRepository;
import com.poliglota.repository.UserRepository;
import com.poliglota.DTO.response.UsuarioResponseDTO;
import com.poliglota.model.mysql.Rol;
import com.poliglota.model.mysql.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioService {

	private final UserRepository usuarioRepository;
	private final SessionRepository sessionRepository;

	@Autowired
	public UsuarioService(UserRepository usuarioRepository,
	                      SessionRepository sessionRepository) {
		this.usuarioRepository = usuarioRepository;
		this.sessionRepository = sessionRepository;
	}

	//  Obtener todos los usuarios
	public List<UsuarioResponseDTO> getTodosLosUsuarios() {
		return usuarioRepository.findAll()
				.stream()
				.map(this::mapToResponseDTO)
				.collect(Collectors.toList());
	}

	public Optional<UsuarioResponseDTO> getUsuarioPorMail(String email) {
		return usuarioRepository.findByEmail(email)
				.map(this::mapToResponseDTO);
	}

	public Optional<UsuarioResponseDTO> getUsuarioPorId(Long id) {
		return usuarioRepository.findById(id)
				.map(this::mapToResponseDTO);
	}

	private UsuarioResponseDTO mapToResponseDTO(User usuario) {
		UsuarioResponseDTO dto = new UsuarioResponseDTO();
		dto.setUserId(usuario.getUserId());
		dto.setNombreCompleto(usuario.getFullName());
		dto.setEmail(usuario.getEmail());
		if(usuario.getRol() == Rol.USUARIO){
			dto.setRol("USUARIO");
		} else if (usuario.getRol() == Rol.ADMIN) {
			dto.setRol("ADMIN");
		} else if (usuario.getRol() == Rol.MANTENIMIENTO) {
			dto.setRol("MANTENIMIENTO");
		}
		return dto;
	}
}
