package com.pos.tenant.infrastructure.adapter.in.telegram;

import com.pos.tenant.domain.port.in.ActivateSubscriptionCommand;
import com.pos.tenant.domain.port.in.ActivateSubscriptionUseCase;
import com.pos.tenant.domain.port.out.SubscriptionRequestRepository;
import com.pos.tenant.domain.port.out.TenantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;
import java.util.UUID;

public class AdminTelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private static final Logger log = LoggerFactory.getLogger(AdminTelegramBot.class);

    private final String botToken;
    private final TelegramClient telegramClient;
    private ActivateSubscriptionUseCase activateSubscriptionUseCase;
    private TenantRepository tenantRepository;
    private SubscriptionRequestRepository subscriptionRequestRepository;

    public AdminTelegramBot(String botToken) {
        this.botToken = botToken;
        this.telegramClient = new OkHttpTelegramClient(botToken);
    }

    public void setActivateSubscriptionUseCase(ActivateSubscriptionUseCase activateSubscriptionUseCase) {
        this.activateSubscriptionUseCase = activateSubscriptionUseCase;
    }

    public void setTenantRepository(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    public void setSubscriptionRequestRepository(SubscriptionRequestRepository subscriptionRequestRepository) {
        this.subscriptionRequestRepository = subscriptionRequestRepository;
    }

    public TelegramClient getTelegramClient() {
        return telegramClient;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        if (update.hasCallbackQuery()) {
            handleCallback(update);
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            handleMessage(update);
        }
    }

    private void handleMessage(Update update) {
        String text = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        if (text.startsWith("/start")) {
            String[] parts = text.split(" ");
            if (parts.length < 2) {
                sendMessage(chatId, "Hola. Envía el link desde tu app para registrarte.");
                return;
            }
            handleStartCommand(chatId, parts[1]);
        }
    }

    private void handleStartCommand(long chatId, String tenantIdStr) {
        try {
            UUID tenantId = UUID.fromString(tenantIdStr);
            if (tenantRepository == null) {
                sendMessage(chatId, "⚠️ Servicio no disponible, intenta más tarde.");
                return;
            }
            tenantRepository.findById(tenantId).ifPresentOrElse(
                    tenant -> {
                        tenantRepository.saveTelegramChatId(tenantId, chatId);
                        sendMessage(chatId, "✅ Registro exitoso. A partir de ahora recibirás alertas de stock de *" + tenant.getName() + "*.");
                        log.info("Tenant {} registered Telegram chatId={}", tenantId, chatId);
                    },
                    () -> sendMessage(chatId, "⚠️ No se encontró el negocio. Verifica el link desde tu app.")
            );
        } catch (IllegalArgumentException e) {
            sendMessage(chatId, "⚠️ Link inválido. Usa el botón desde tu app.");
        }
    }

    private void handleCallback(Update update) {
        String data = update.getCallbackQuery().getData();
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        if (data.startsWith("activate:")) {
            String[] parts = data.split(":");
            UUID tenantId = UUID.fromString(parts[1]);
            String planName = parts[2];
            handleActivate(chatId, tenantId, planName);
        } else if (data.startsWith("reject:")) {
            UUID tenantId = UUID.fromString(data.split(":")[1]);
            handleReject(chatId, tenantId);
        }
    }

    private void handleReject(long chatId, UUID tenantId) {
        if (subscriptionRequestRepository == null) {
            sendMessage(chatId, "⚠️ Servicio no disponible, intenta más tarde.");
            return;
        }
        try {
            subscriptionRequestRepository.findPendingByTenantId(tenantId).ifPresentOrElse(
                    request -> {
                        request.reject();
                        subscriptionRequestRepository.save(request);
                        sendMessage(chatId, "❌ Solicitud rechazada.\nTenant: " + tenantId);
                        log.info("Subscription request rejected — tenantId={}", tenantId);
                    },
                    () -> sendMessage(chatId, "⚠️ No se encontró solicitud pendiente para este tenant.")
            );
        } catch (Exception e) {
            log.error("Error rejecting subscription request — tenantId={}", tenantId, e);
            sendMessage(chatId, "⚠️ Error al rechazar: " + e.getMessage());
        }
    }

    private void handleActivate(long chatId, UUID tenantId, String planName) {
        try {
            activateSubscriptionUseCase.activate(new ActivateSubscriptionCommand(tenantId, planName));
            sendMessage(chatId, "✅ Suscripción activada correctamente.\nTenant: " + tenantId + "\nPlan: " + planName);
        } catch (Exception e) {
            log.error("Error activating subscription — tenantId={}", tenantId, e);
            sendMessage(chatId, "⚠️ Error al activar: " + e.getMessage());
        }
    }

    public void sendPaymentRequestNotification(long adminChatId, UUID requestId, UUID tenantId,
                                                String tenantName, String planName,
                                                String transactionRef, String receiptUrl) {
        log.info("Building Telegram notification — receiptUrl={}", receiptUrl);
        String text = String.format(
                "🔔 <b>Nueva solicitud de suscripción</b>\n\n" +
                "👤 Negocio: %s\n" +
                "📦 Plan: <b>%s</b>\n" +
                "🔖 Referencia: %s\n" +
                "🪪 TenantID: <code>%s</code>\n" +
                "🧾 <a href=\"%s\">Ver comprobante</a>\n" +
                "\n¿Qué deseas hacer?",
                tenantName, planName, transactionRef != null ? transactionRef : "—",
                tenantId, receiptUrl
        );

        InlineKeyboardMarkup keyboard = InlineKeyboardMarkup.builder()
                .keyboardRow(new InlineKeyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text("✅ Activar")
                                .callbackData("activate:" + tenantId + ":" + planName)
                                .build(),
                        InlineKeyboardButton.builder()
                                .text("❌ Rechazar")
                                .callbackData("reject:" + tenantId)
                                .build()
                )))
                .build();

        SendMessage message = SendMessage.builder()
                .chatId(adminChatId)
                .text(text)
                .parseMode("HTML")
                .replyMarkup(keyboard)
                .build();

        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            log.error("Failed to send Telegram notification to admin", e);
        }
    }

    public void sendTextMessage(long chatId, String text) {
        try {
            telegramClient.execute(SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .parseMode("HTML")
                    .build());
        } catch (TelegramApiException e) {
            log.error("Failed to send Telegram text message", e);
        }
    }

    private void sendMessage(long chatId, String text) {
        try {
            telegramClient.execute(SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .build());
        } catch (TelegramApiException e) {
            log.error("Failed to send Telegram message", e);
        }
    }
}
