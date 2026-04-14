package com.pos.errorlog.infrastructure.config;

import com.pos.errorlog.domain.port.in.CreateErrorLogUseCase;
import com.pos.errorlog.domain.port.out.ErrorLogRepository;
import com.pos.errorlog.domain.service.CreateErrorLogService;
import com.pos.errorlog.infrastructure.adapter.out.persistence.ErrorLogRepositoryAdapter;
import com.pos.errorlog.infrastructure.adapter.out.persistence.repository.JpaErrorLogRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorLogBeanConfig {

    @Bean
    public ErrorLogRepository errorLogRepository(JpaErrorLogRepository jpa) {
        return new ErrorLogRepositoryAdapter(jpa);
    }

    @Bean
    public CreateErrorLogUseCase createErrorLogUseCase(ErrorLogRepository repository) {
        return new CreateErrorLogService(repository);
    }
}
