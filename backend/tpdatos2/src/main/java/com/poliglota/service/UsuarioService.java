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

	@Autowired
	public UsuarioService(UsuarioRepository usuarioRepository, ProductoRepository productoRepository) {
		this.usuarioRepository = usuarioRepository;
		this.productoRepository = productoRepository;
	}

	public List<UsuarioResponseDTO> getTodosLosUsuarios() {
		return usuarioRepository.findAll()
				.stream()
				.map(this::mapToResponseDTO)
				.toList();
	}

	public Optional<UsuarioResponseDTO> getUsuarioPorMail(String email) {
		return usuarioRepository.findByEmail(email)
				.map(this::mapToResponseDTO);
	}

	public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioUpdateRequestDTO dto) {
		Usuario usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con id: " + id));

		usuario.setNombreCompleto(dto.getNombreCompleto());
		usuario.setDireccion(dto.getDireccion());
		usuario.setTelefono(dto.getTelefono());
		usuario.setFechaNacimiento(dto.getFechaNacimiento());

		return mapToResponseDTO(usuarioRepository.save(usuario));
	}

	public void eliminarUsuario(Long id) {
		if (!usuarioRepository.existsById(id)) {
			throw new UsuarioNotFoundException("Usuario no encontrado con id: " + id);
		}
		usuarioRepository.deleteById(id);
	}


	private UsuarioResponseDTO mapToResponseDTO(Usuario usuario) {
		UsuarioResponseDTO dto = new UsuarioResponseDTO();
		dto.setId(usuario.getId());
		dto.setNombreCompleto(usuario.getNombreCompleto());
		dto.setDireccion(usuario.getDireccion());
		dto.setTelefono(usuario.getTelefono());
		dto.setFechaNacimiento(usuario.getFechaNacimiento());
		dto.setEmail(usuario.getEmail());
		return dto;
	}

}