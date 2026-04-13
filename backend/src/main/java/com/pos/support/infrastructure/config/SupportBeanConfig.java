package com.pos.support.infrastructure.config;

import com.pos.support.domain.port.in.ListReportsUseCase;
import com.pos.support.domain.port.in.SubmitReportUseCase;
import com.pos.support.domain.port.out.SupportNotificationPort;
import com.pos.support.domain.port.out.SupportReportRepository;
import com.pos.support.domain.service.ListReportsService;
import com.pos.support.domain.service.SubmitReportService;
import com.pos.support.infrastructure.adapter.out.TelegramSupportNotificationAdapter;
import com.pos.support.infrastructure.adapter.out.persistence.memory.InMemorySupportReportRepository;
import com.pos.tenant.domain.port.out.TenantRepository;
import com.pos.tenant.infrastructure.adapter.in.telegram.AdminTelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("memory")
public class SupportBeanConfig {

    @Value("${telegram.admin-chat-id:0}")
    private long adminChatId;

    @Bean
    public SupportReportRepository supportReportRepository() {
        return new InMemorySupportReportRepository();
    }

    @Bean
    public SupportNotificationPort supportNotificationPort(AdminTelegramBot bot) {
        return new TelegramSupportNotificationAdapter(bot, adminChatId);
    }

    @Bean
    public SubmitReportUseCase submitReportUseCase(SupportReportRepository repository,
                                                   SupportNotificationPort notification,
                                                   TenantRepository tenantRepository) {
        return new SubmitReportService(repository, notification, tenantRepository);
    }

    @Bean
    public ListReportsUseCase listReportsUseCase(SupportReportRepository repository) {
        return new ListReportsService(repository);
    }
}
