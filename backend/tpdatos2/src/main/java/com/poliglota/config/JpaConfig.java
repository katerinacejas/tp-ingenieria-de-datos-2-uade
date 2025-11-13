package com.poliglota.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackages = "com.poliglota.model.mysql")
public class JpaConfig {

}
