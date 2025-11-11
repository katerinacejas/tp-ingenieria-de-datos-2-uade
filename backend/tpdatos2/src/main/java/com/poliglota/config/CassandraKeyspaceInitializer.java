package com.poliglota.config;

import java.net.InetSocketAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import com.datastax.oss.driver.api.core.CqlSession;

@Configuration
public class CassandraKeyspaceInitializer implements CommandLineRunner {

    @Value("${spring.cassandra.contact-points:localhost}")
    private String contactPoint;

    @Value("${spring.cassandra.port:9042}")
    private int port;

    @Value("${spring.cassandra.local-datacenter:datacenter1}")
    private String datacenter;

    @Override
    public void run(String... args) {
        try (CqlSession session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress(contactPoint, port))
                .withLocalDatacenter(datacenter)
                .build()) {

            session.execute("""
                CREATE KEYSPACE IF NOT EXISTS poliglota_keyspace
                WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1}
            """);
        }
    }
}
