package com.poliglota;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import com.poliglota.vista.VistaCompartida;

@SpringBootApplication
@EntityScan(basePackages = "com.poliglota.model.mysql")
@EnableJpaRepositories(basePackages = "com.poliglota.repository.jpa")
@EnableMongoRepositories(basePackages = "com.poliglota.repository.mongo")
@EnableCassandraRepositories(basePackages = "com.poliglota.repository.cassandra")
public class BackendPoliglotaApplication{
    public static void main(String[] args) {
        SpringApplication.run(BackendPoliglotaApplication.class, args);
    }
}
