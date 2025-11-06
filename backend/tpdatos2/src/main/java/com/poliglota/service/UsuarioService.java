package com.poliglota.service;


import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;
	private final ProductoRepository productoRepository;
	private final SessionRepository sessionRepository;

	@Autowired
	public UsuarioService(UsuarioRepository usuarioRepository,
	                      ProductoRepository productoRepository,
	                      SessionRepository sessionRepository) {
		this.usuarioRepository = usuarioRepository;
		this.productoRepository = productoRepository;
		this.sessionRepository = sessionRepository;
	}

	// 🔹 Crear usuario con rol
	public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO dto) {
		Usuario usuario = new Usuario();
		usuario.setNombreCompleto(dto.getNombreCompleto());
		usuario.setEmail(dto.getEmail());
		usuario.setPassword(dto.getPassword());
		usuario.setDireccion(dto.getDireccion());
		usuario.setTelefono(dto.getTelefono());
		usuario.setRol(dto.getRol());
		usuario.setActivo(true);
		return mapToResponseDTO(usuarioRepository.save(usuario));
	}

	// 🔹 Obtener todos los usuarios
	public List<UsuarioResponseDTO> getTodosLosUsuarios() {
		return usuarioRepository.findAll()
				.stream()
				.map(this::mapToResponseDTO)
				.collect(Collectors.toList());
	}

	// 🔹 Buscar usuarios por rol
	public List<UsuarioResponseDTO> getUsuariosPorRol(Rol rol) {
		return usuarioRepository.findByRol(rol)
				.stream()
				.map(this::mapToResponseDTO)
				.collect(Collectors.toList());
	}

	// 🔹 Login (crea sesión activa)
	public boolean iniciarSesion(String email, String password) {
		Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

		if (usuarioOpt.isEmpty() || !usuarioOpt.get().getPassword().equals(password)) {
			return false;
		}

		Usuario usuario = usuarioOpt.get();

		Session session = new Session();
		session.setUserId(usuario.getId().toString());
		session.setRoleId(usuario.getRol().name());
		session.setStartTime(LocalDateTime.now());
		session.setStatus("active");
		sessionRepository.save(session);

		return true;
	}

	// 🔹 Cerrar sesión
	public void cerrarSesion(Long userId) {
		sessionRepository.findByUserIdAndStatus(userId.toString(), "active")
				.forEach(s -> {
					s.setStatus("inactive");
					s.setEndTime(LocalDateTime.now());
					sessionRepository.save(s);
				});
	}

	// 🔹 Actualizar usuario 
	public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioUpdateRequestDTO dto) {
		Usuario usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id));

		usuario.setNombreCompleto(dto.getNombreCompleto());
		usuario.setDireccion(dto.getDireccion());
		usuario.setTelefono(dto.getTelefono());
		usuario.setFechaNacimiento(dto.getFechaNacimiento());

		return mapToResponseDTO(usuarioRepository.save(usuario));
	}

	// 🔹 Eliminar usuario
	public void eliminarUsuario(Long id) {
		if (!usuarioRepository.existsById(id)) {
			throw new EntityNotFoundException("Usuario no encontrado con id: " + id);
		}
		usuarioRepository.deleteById(id);
	}

	// 🔹 DTO mapper
	private UsuarioResponseDTO mapToResponseDTO(Usuario usuario) {
		UsuarioResponseDTO dto = new UsuarioResponseDTO();
		dto.setId(usuario.getId());
		dto.setNombreCompleto(usuario.getNombreCompleto());
		dto.setDireccion(usuario.getDireccion());
		dto.setTelefono(usuario.getTelefono());
		dto.setFechaNacimiento(usuario.getFechaNacimiento());
		dto.setEmail(usuario.getEmail());
		dto.setRol(usuario.getRol());
		return dto;
	}
}
