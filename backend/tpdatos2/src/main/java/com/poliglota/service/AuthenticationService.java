package com.poliglota.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.poliglota.DTO.request.LoginRequestDTO;
import com.poliglota.DTO.request.RegistroRequestDTO;
import com.poliglota.DTO.response.JwtResponseDTO;
import com.poliglota.model.mysql.Rol;
import com.poliglota.model.mysql.User;
import com.poliglota.repository.mysql.UserRepository;
import com.poliglota.security.JwtUtil;

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

            String encryptedPassword = passwordEncoder.encode(request.getPassword());
            nuevoUsuario.setPassword(encryptedPassword);

            nuevoUsuario.setRol(Rol.USUARIO);

            System.out.println("Rol asignado al usuario: " + nuevoUsuario.getRol());

            usuarioRepository.save(nuevoUsuario);
			System.out.println("guarde un usuario");

            String token = jwtUtil.generateToken(nuevoUsuario.getEmail(), Rol.USUARIO.name());
            return new JwtResponseDTO(token, Rol.USUARIO);

        } catch (Exception e) {
            throw new RuntimeException("Error al registrar usuario: " + e.getMessage());
        }
    }
}