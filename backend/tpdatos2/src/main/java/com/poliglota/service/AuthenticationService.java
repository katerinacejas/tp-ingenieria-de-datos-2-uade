package com.poliglota.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.poliglota.DTO.SessionDTO;
import com.poliglota.DTO.request.LoginRequestDTO;
import com.poliglota.DTO.request.RegistroRequestDTO;
import com.poliglota.DTO.response.JwtResponseDTO;
import com.poliglota.model.mysql.RolEntity;
import com.poliglota.model.mysql.Account;
import com.poliglota.model.mysql.Rol;
import com.poliglota.model.mysql.User;
import com.poliglota.repository.AccountRepository;
import com.poliglota.repository.RolRepository;
import com.poliglota.repository.UserRepository;
import com.poliglota.security.JwtUtil;
import com.poliglota.model.mysql.Session;
import com.poliglota.repository.SessionRepository;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

	@Autowired
    private RolRepository rolRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private SessionRepository sessionRepository;

    public JwtResponseDTO authenticate(LoginRequestDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
                )
            );

            User usuario = (User) authentication.getPrincipal();
			
			String rolString = usuario.getRol().name();
            String jwt = jwtUtil.generateToken(usuario.getEmail(), rolString);
			Rol rol = usuario.getRol();

			usuario.setStatus("activo");
			usuarioRepository.save(usuario);

            return new JwtResponseDTO(jwt, rol);

        } catch (AuthenticationException ex) {
            throw new RuntimeException("Credenciales inválidas");
        }
    }

    public JwtResponseDTO register(RegistroRequestDTO request) {
        try {
            if (usuarioRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Ya existe un usuario con ese email");
            }

            User nuevoUsuario = new User();
            nuevoUsuario.setFullName(request.getNombreCompleto());
            nuevoUsuario.setEmail(request.getEmail());

			Account account = new Account();
			account.setUserId(nuevoUsuario);
			account.setCurrentBalance(0.0);
			accountRepository.save(account);

            String encryptedPassword = passwordEncoder.encode(request.getPassword());
            nuevoUsuario.setPassword(encryptedPassword);

            RolEntity rolUsuario = rolRepository.findByCode(Rol.USUARIO)
                .orElseThrow(() -> new IllegalStateException("Rol USUARIO no configurado"));
            nuevoUsuario.setRolEntity(rolUsuario);

            System.out.println("Rol asignado al usuario: " + nuevoUsuario.getRol());

			nuevoUsuario.setStatus("activo");
			nuevoUsuario.setRegisteredAt(LocalDateTime.now());

            usuarioRepository.save(nuevoUsuario);
			System.out.println("guarde un usuario");

            String token = jwtUtil.generateToken(nuevoUsuario.getEmail(), Rol.USUARIO.name());
            return new JwtResponseDTO(token, Rol.USUARIO);

        } catch (Exception e) {
            throw new RuntimeException("Error al registrar usuario: " + e.getMessage());
        }
    }

	public SessionDTO closeSession(String email) {
		User user = usuarioRepository.findByEmail(email).orElse(null);
		if (user != null) {
			user.setStatus("inactivo");
			usuarioRepository.save(user);
			SessionDTO sessionDTO = new SessionDTO();
			Session session = sessionRepository.findByUserIdAndStatus(user.getUserId().toString(), "activa").get(0);
			sessionDTO.setUserId(user.getUserId().toString());
			return sessionDTO;
		} 
		return null;
	}
}