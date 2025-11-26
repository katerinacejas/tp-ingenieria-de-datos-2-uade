package com.poliglota.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.poliglota.model.mysql.Process;
import com.poliglota.model.mysql.*;
import com.poliglota.model.mongo.*;
import com.poliglota.model.cassandra.*;
import com.poliglota.repository.cassandra.MeasurementRepository;
import com.poliglota.repository.jpa.AccountMovementHistoryRepository;
import com.poliglota.repository.jpa.AccountRepository;
import com.poliglota.repository.jpa.InvoiceRepository;
import com.poliglota.repository.jpa.PaymentRepository;
import com.poliglota.repository.jpa.ProcessRepository;
import com.poliglota.repository.jpa.RolRepository;
import com.poliglota.repository.jpa.SessionRepository;
import com.poliglota.repository.jpa.UserRepository;
import com.poliglota.repository.mongo.AlertsRepository;
import com.poliglota.repository.mongo.GroupRepository;
import com.poliglota.repository.mongo.MaintenanceCheckRepository;
import com.poliglota.repository.mongo.MessageRepository;
import com.poliglota.repository.mongo.SensorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;
import java.time.Instant;
import java.util.List;

import java.time.ZoneOffset;


@Configuration
public class DataInitializer {
/*

    @Bean
    CommandLineRunner seedAllData(
            RolRepository rolRepository,
            UserRepository userRepository,
            AccountRepository accountRepository,
            AccountMovementHistoryRepository movementRepository,
            InvoiceRepository invoiceRepository,
            PaymentRepository paymentRepository,
            ProcessRepository processRepository,
            SessionRepository sessionRepository,

            SensorRepository sensorRepository,
            AlertsRepository alertsRepository,
            MessageRepository messageRepository,
            GroupRepository groupRepository,
            MaintenanceCheckRepository maintenanceRepository,

            MeasurementRepository measurementRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            System.out.println("Inicializando datos de MySQL, MongoDB y Cassandra...");

            seedMySQL(rolRepository, userRepository, accountRepository,
                    movementRepository, invoiceRepository, paymentRepository,
                    processRepository, sessionRepository, passwordEncoder);

            seedMongo(sensorRepository, alertsRepository, messageRepository,
                    groupRepository, maintenanceRepository);

            seedCassandra(measurementRepository);

            System.out.println("Todas las bases de datos fueron pobladas correctamente.");
        };
    }

    // ============================================================
    //  MySQL (Relacional)
    // ============================================================
    private void seedMySQL(
            RolRepository rolRepo,
            UserRepository userRepo,
            AccountRepository accountRepo,
            AccountMovementHistoryRepository movRepo,
            InvoiceRepository invoiceRepo,
            PaymentRepository paymentRepo,
            ProcessRepository processRepo,
            SessionRepository sessionRepo,
            PasswordEncoder encoder
    ) {
        if (rolRepo.count() == 0) {
            rolRepo.saveAll(List.of(
                    new RolEntity(null, Rol.ADMIN, "Administrador del sistema"),
                    new RolEntity(null, Rol.USUARIO, "Usuario común del sistema"),
                    new RolEntity(null, Rol.MANTENIMIENTO, "Técnico de mantenimiento")
            ));
        }

        RolEntity rolAdmin = rolRepo.findByCode(Rol.ADMIN).orElseThrow();
        RolEntity rolUser = rolRepo.findByCode(Rol.USUARIO).orElseThrow();
        RolEntity rolTech = rolRepo.findByCode(Rol.MANTENIMIENTO).orElseThrow();

        if (!userRepo.existsByEmail("admin@tpdatos2.com")) {
            User admin = new User(null, "Administrador", "admin@tpdatos2.com",
                    encoder.encode("Admin123!"), "activo", LocalDateTime.now(), rolAdmin);
            userRepo.save(admin);
        }

        if (!userRepo.existsByEmail("juan@correo.com")) {
            User user = new User(null, "Juan Pérez", "juan@correo.com",
                    encoder.encode("User123!"), "activo", LocalDateTime.now(), rolUser);
            userRepo.save(user);
        }

        if (!userRepo.existsByEmail("tecnico@tpdatos2.com")) {
            User tech = new User(null, "Técnico de Mantenimiento", "tecnico@tpdatos2.com",
                    encoder.encode("Tech123!"), "activo", LocalDateTime.now(), rolTech);
            userRepo.save(tech);
        }

        User user = userRepo.findByEmail("juan@correo.com").orElseThrow();

        Account account = new Account(null, user, 15000.00);
        accountRepo.save(account);
        movRepo.save(new AccountMovementHistory(null, account, 2000.00, "DEPOSIT", 0.00, 2000.00, LocalDateTime.now()));

        com.poliglota.model.mysql.Process process = new com.poliglota.model.mysql.Process(null, "Mantenimiento Preventivo", "Chequeo técnico general", "SERVICE", 1200.00);
        processRepo.save(process);

        Invoice invoice = new Invoice(null, user, LocalDateTime.now(), List.of(), "PENDING");
        invoiceRepo.save(invoice);

        Payment payment = new Payment(null, invoice, LocalDateTime.now(), 1200.00, "TRANSFERENCIA");
        paymentRepo.save(payment);

        Session session = new Session(null, user, rolUser, LocalDateTime.now(), null, "ACTIVE");
        sessionRepo.save(session);

        System.out.println(" Datos MySQL cargados.");
    }

    // ============================================================
    //  MongoDB (Documental)
    // ============================================================
    private void seedMongo(
            SensorRepository sensorRepo,
            AlertsRepository alertsRepo,
            MessageRepository messageRepo,
            GroupRepository groupRepo,
            MaintenanceCheckRepository maintenanceRepo
    ) {
        if (sensorRepo.count() > 0) return;

        Sensor sensor1 = new Sensor(null, "Sensor BA-001", "TEMPERATURE", "Buenos Aires", "Argentina", "inactivo", LocalDateTime.now());
        Sensor sensor2 = new Sensor(null, "Sensor CBA-002", "HUMIDITY", "Córdoba", "Argentina", "inactivo", LocalDateTime.now());
        sensorRepo.saveAll(List.of(sensor1, sensor2));

        MaintenanceCheck check1 = new MaintenanceCheck("CHK-001", sensor1.getId(), LocalDateTime.now(), "OK", "Chequeo general correcto");
        MaintenanceCheck check2 = new MaintenanceCheck("CHK-002", sensor2.getId(), LocalDateTime.now(), "OK", "Lectura de humedad estable");
        maintenanceRepo.saveAll(List.of(check1, check2));

        Alerts alert = new Alerts("ALERT-001", "TEMPERATURE_WARNING", "ACTIVE", sensor1.getId(), LocalDateTime.now(), "Temperatura elevada detectada");
        alertsRepo.save(alert);

        System.out.println(" Datos MongoDB cargados.");
    }

    // ============================================================
    //  Cassandra (Columnar)
    // ============================================================
    private void seedCassandra(MeasurementRepository measurementRepo) {
        if (measurementRepo.count() > 0) return;
        Measurement m1 = new Measurement();
        m1.setKey(new MeasurementKey("SENSOR_001", Instant.now()));
        m1.setTemperature(23.5);
        m1.setHumidity(60.0);
        Measurement m2 = new Measurement();
        m2.setKey(new MeasurementKey("SENSOR_002", Instant.now()));
        m2.setTemperature(19.2);
        m2.setHumidity(72.0);

        measurementRepo.saveAll(List.of(m1, m2));

        System.out.println(" Datos Cassandra cargados.");
    }
		*/
}
