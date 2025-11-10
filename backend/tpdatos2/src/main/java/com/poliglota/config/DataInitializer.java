package com.poliglota.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.poliglota.model.mysql.Rol;
import com.poliglota.model.mysql.User;
import com.poliglota.repository.mysql.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        String adminEmail = "admin@tpdatos2.com";
        if (!usuarioRepository.existsByEmail(adminEmail)) {
            User admin = new User();
            admin.setFullName("Administrador");
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("Admin123!"));
            admin.setRol(Rol.ADMIN);
            usuarioRepository.save(admin);
            System.out.println("Usuario ADMIN creado: " + adminEmail + " / Admin123!");
        }
    }
}