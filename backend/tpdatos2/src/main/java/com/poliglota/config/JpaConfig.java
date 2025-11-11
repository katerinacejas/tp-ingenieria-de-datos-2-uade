package com.poliglota.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.poliglota.repository.mysql")
@EntityScan(basePackages = "com.poliglota.model.mysql")
public class JpaConfig {

}
