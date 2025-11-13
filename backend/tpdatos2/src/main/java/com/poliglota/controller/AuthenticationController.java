package com.poliglota.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
	public ResponseEntity<String> login(@RequestBody LoginRequestDTO request) {
		try {
			String ok = authenticationService.authenticate(request);
			if (ok == null) {
				return ResponseEntity.status(401).body("Credenciales inválidas");
			}
			else {
				return ResponseEntity.status(401).body(ok);
			}
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Error en autenticación: " + e.getMessage());
		}
	}
/*
	public ResponseEntity<String> login(@RequestBody LoginRequestDTO request) {
		try {
			JwtResponseDTO jwtResponseDTO = authenticationService.authenticate(request);
			if (jwtResponseDTO == null) {
				return ResponseEntity.status(401).body("Credenciales inválidas");
			}

			UsuarioResponseDTO usuarioResponseDTO = usuarioService.getUsuarioPorMail(request.getEmail()).orElse(null);
			SessionDTO sessionDTO = new SessionDTO();
			sessionDTO.setUserId(usuarioResponseDTO.getUserId().toString());
			sessionDTO.setRolId(usuarioResponseDTO.getRol());
			sessionDTO.setStartTime(LocalDateTime.now());
			sessionDTO.setEndTime(null);
			sessionDTO.setStatus("activa");
			sessionService.createSession(sessionDTO);

			return ResponseEntity.ok(jwtResponseDTO.getToken());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Error en autenticación: " + e.getMessage());
		}
	}*/

	@PostMapping("/logout")
    public ResponseEntity<String> logout(Authentication auth) {
		String email = auth.getName();
		SessionDTO sessionDTO = authenticationService.closeSession(email);
        sessionService.closeSession(sessionDTO);
        return ResponseEntity.ok("Sesión cerrada");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistroRequestDTO request) {
		try {
			String ok = authenticationService.register(request);
			if (ok == null) {
				UsuarioResponseDTO usuarioResponseDTO = usuarioService.getUsuarioPorMail(request.getEmail()).orElse(null);
				SessionDTO sessionDTO = new SessionDTO();
				sessionDTO.setUserId(usuarioResponseDTO.getUserId().toString());
				sessionDTO.setRolId(usuarioResponseDTO.getRol());
				sessionDTO.setStartTime(LocalDateTime.now());
				sessionDTO.setEndTime(null);
				sessionDTO.setStatus("activa");
				sessionService.createSession(sessionDTO);
				return ResponseEntity.status(401).body("Credenciales inválidas");
			}
			else {
				return ResponseEntity.status(401).body(ok);
			}
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Error en registro de usuario: " + e.getMessage());
		}
    }
}
