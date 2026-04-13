package com.pos.tenant.infrastructure.adapter.out.telegram;

import com.pos.tenant.domain.model.SubscriptionRequest;
import com.pos.tenant.domain.port.out.AdminNotificationPort;
import com.pos.tenant.infrastructure.adapter.in.telegram.AdminTelegramBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TelegramAdminNotificationAdapter implements AdminNotificationPort {

    private static final Logger log = LoggerFactory.getLogger(TelegramAdminNotificationAdapter.class);

    private final AdminTelegramBot bot;
    private final long adminChatId;

    public TelegramAdminNotificationAdapter(AdminTelegramBot bot, long adminChatId) {
        this.bot = bot;
        this.adminChatId = adminChatId;
    }

    @Override
    public void notifyNewPaymentRequest(SubscriptionRequest request, String tenantName) {
        log.info("Notifying admin — requestId={} tenant={} receiptUrl={}", request.getId(), tenantName, request.getReceiptUrl());
        bot.sendPaymentRequestNotification(
                adminChatId,
                request.getId(),
                request.getTenantId(),
                tenantName,
                request.getPlanName(),
                request.getTransactionRef(),
                request.getReceiptUrl()
        );
    }

    @Override
    public void notifyActivationResult(String tenantPhone, boolean approved, String planName) {
        // Notificación al cliente se implementará en fase 5 (cuando tengamos telegram_chat_id del cliente)
        log.info("Subscription {} for phone={} plan={}", approved ? "approved" : "rejected", tenantPhone, planName);
    }
}
