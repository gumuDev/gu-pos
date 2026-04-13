package com.pos.migration.infrastructure.config;

import com.pos.migration.domain.service.MigrationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class MigrationBeanConfig {

    @Bean
    public MigrationService migrationService(JdbcTemplate jdbcTemplate) {
        return new MigrationService(jdbcTemplate);
    }
}
