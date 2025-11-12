package com.poliglota;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.poliglota.repository")
@EntityScan(basePackages = "com.poliglota.model.mysql")
public class BackendPoliglotaApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackendPoliglotaApplication.class, args);
    }
}
