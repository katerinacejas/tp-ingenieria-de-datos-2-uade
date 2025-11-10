package com.poliglota.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.poliglota.model.mysql.RolEntity;
import com.poliglota.model.mysql.Rol;
import com.poliglota.model.mysql.User;
import com.poliglota.repository.RolRepository;
import com.poliglota.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedData(
            RolRepository roleRepo,
            UserRepository userRepo,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            ensureRole(roleRepo, Rol.USUARIO,        "Usuario cliente");
            ensureRole(roleRepo, Rol.MANTENIMIENTO,  "Personal de mantenimiento");
            ensureRole(roleRepo, Rol.ADMIN,          "Administrador");

            final String adminEmail = "admin@tpdatos2.com";
            if (!userRepo.existsByEmail(adminEmail)) {
                RolEntity roleAdmin = roleRepo.findByCode(Rol.ADMIN)
                        .orElseThrow(() -> new IllegalStateException("Rol ADMIN no configurado"));

                User admin = new User();
                admin.setFullName("Administrador");
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode("Admin123!"));
                // asignar la entidad de rol (no el enum directo)
                admin.setRolEntity(roleAdmin);

                userRepo.save(admin);
                System.out.println("Usuario ADMIN creado: " + adminEmail + " / Admin123!");
            }
        };
    }

    private void ensureRole(RolRepository repo, Rol code, String descripcion) {
        repo.findByCode(code).orElseGet(() -> {
            RolEntity r = new RolEntity();
            r.setCode(code);
            r.setDescripcion(descripcion);
            return repo.save(r);
        });
    }
}
