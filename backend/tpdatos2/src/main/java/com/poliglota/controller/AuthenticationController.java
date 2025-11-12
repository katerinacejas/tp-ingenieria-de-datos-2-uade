package com.poliglota.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.poliglota.service.AuthenticationService;
import com.poliglota.service.SessionService;
import com.poliglota.DTO.SessionDTO;
import com.poliglota.DTO.request.LoginRequestDTO;
import com.poliglota.DTO.request.RegistroRequestDTO;
import com.poliglota.DTO.response.JwtResponseDTO;
import com.poliglota.DTO.response.UsuarioResponseDTO;
import com.poliglota.service.UsuarioService;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private SessionService sessionService;

	@PostMapping("/login")
	public JwtResponseDTO login(@RequestBody LoginRequestDTO request) {
		JwtResponseDTO jwtResponseDTO = authenticationService.authenticate(request);
		if (jwtResponseDTO != null) {
			UsuarioResponseDTO usuarioResponseDTO = usuarioService.getUsuarioPorMail(request.getEmail()).orElse(null);
			SessionDTO sessionDTO = new SessionDTO();
			sessionDTO.setUserId(usuarioResponseDTO.getUserId().toString());
			sessionDTO.setRolId(usuarioResponseDTO.getRol());
			sessionDTO.setStartTime(LocalDateTime.now());
			sessionDTO.setEndTime(null);
			sessionDTO.setStatus("activa");
			sessionService.createSession(sessionDTO);
			return jwtResponseDTO;
		}
		return null;
	}

	@PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody SessionDTO sessionDTO) {
        sessionService.closeSession(sessionDTO);
        return ResponseEntity.ok("Sesión cerrada");
    }

    @PostMapping("/register")
    public JwtResponseDTO register(@RequestBody RegistroRequestDTO request) {
		JwtResponseDTO jwtResponseDTO = authenticationService.register(request);
		if (jwtResponseDTO != null) {
			UsuarioResponseDTO usuarioResponseDTO = usuarioService.getUsuarioPorMail(request.getEmail()).orElse(null);
			SessionDTO sessionDTO = new SessionDTO();
			sessionDTO.setUserId(usuarioResponseDTO.getUserId().toString());
			sessionDTO.setRolId(usuarioResponseDTO.getRol());
			sessionDTO.setStartTime(LocalDateTime.now());
			sessionDTO.setEndTime(null);
			sessionDTO.setStatus("activa");
			sessionService.createSession(sessionDTO);
			return jwtResponseDTO;
		}
        return null;
    }
}
