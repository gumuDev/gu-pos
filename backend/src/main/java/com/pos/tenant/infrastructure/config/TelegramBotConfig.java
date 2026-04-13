package com.pos.tenant.infrastructure.config;

import com.pos.tenant.domain.model.SubscriptionRequest;
import com.pos.tenant.domain.port.in.ActivateSubscriptionUseCase;
import com.pos.tenant.domain.port.out.AdminNotificationPort;
import com.pos.tenant.domain.port.out.StockAlertPort;
import com.pos.tenant.domain.port.out.SubscriptionRequestRepository;
import com.pos.tenant.domain.port.out.TenantRepository;
import com.pos.tenant.infrastructure.adapter.in.telegram.AdminTelegramBot;
import com.pos.tenant.infrastructure.adapter.out.telegram.TelegramAdminNotificationAdapter;
import com.pos.tenant.infrastructure.adapter.out.telegram.TelegramStockAlertAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfig {

    private static final Logger log = LoggerFactory.getLogger(TelegramBotConfig.class);

    @Value("${telegram.bot-token:disabled}")
    private String botToken;

    @Value("${telegram.admin-chat-id:0}")
    private long adminChatId;

    @Bean
    @ConditionalOnExpression("!'${telegram.bot-token:disabled}'.equals('disabled')")
    public AdminTelegramBot adminTelegramBot() {
        log.info("Telegram bot enabled — registering AdminTelegramBot");
        return new AdminTelegramBot(botToken);
    }

    @Bean
    @ConditionalOnExpression("!'${telegram.bot-token:disabled}'.equals('disabled')")
    public AdminNotificationPort telegramAdminNotificationPort(
            AdminTelegramBot bot,
            ActivateSubscriptionUseCase activateSubscriptionUseCase,
            TenantRepository tenantRepository,
            SubscriptionRequestRepository subscriptionRequestRepository) {
        bot.setActivateSubscriptionUseCase(activateSubscriptionUseCase);
        bot.setTenantRepository(tenantRepository);
        bot.setSubscriptionRequestRepository(subscriptionRequestRepository);
        return new TelegramAdminNotificationAdapter(bot, adminChatId);
    }

    @Bean
    @ConditionalOnExpression("!'${telegram.bot-token:disabled}'.equals('disabled')")
    public StockAlertPort telegramStockAlertPort(AdminTelegramBot bot) {
        return new TelegramStockAlertAdapter(bot);
    }

    @Bean
    @ConditionalOnExpression("'${telegram.bot-token:disabled}'.equals('disabled')")
    public AdminNotificationPort noOpAdminNotificationPort() {
        log.warn("Telegram bot disabled — TELEGRAM_BOT_TOKEN not set. Notifications will be skipped.");
        return new AdminNotificationPort() {
            @Override
            public void notifyNewPaymentRequest(SubscriptionRequest request, String tenantName) {}
            @Override
            public void notifyActivationResult(String tenantPhone, boolean approved, String planName) {}
        };
    }

    @Bean
    @ConditionalOnExpression("'${telegram.bot-token:disabled}'.equals('disabled')")
    public StockAlertPort noOpStockAlertPort() {
        return (chatId, businessName, productName, currentStock) -> {};
    }
}
