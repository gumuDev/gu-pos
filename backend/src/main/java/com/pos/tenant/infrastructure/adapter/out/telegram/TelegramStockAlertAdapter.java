package com.pos.tenant.infrastructure.adapter.out.telegram;

import com.pos.tenant.domain.port.out.StockAlertPort;
import com.pos.tenant.infrastructure.adapter.in.telegram.AdminTelegramBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramStockAlertAdapter implements StockAlertPort {

    private static final Logger log = LoggerFactory.getLogger(TelegramStockAlertAdapter.class);

    private final AdminTelegramBot bot;

    public TelegramStockAlertAdapter(AdminTelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public void sendLowStockAlert(Long chatId, String businessName, String productName, int currentStock) {
        String text = String.format(
                "⚠️ *Stock bajo — %s*\n\nProducto: %s\nStock actual: %d unidades\nUmbral mínimo: 5 unidades\n\nRevisa tu inventario.",
                businessName, productName, currentStock
        );

        SendMessage message = SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .parseMode("Markdown")
                .build();

        try {
            bot.getTelegramClient().execute(message);
        } catch (TelegramApiException e) {
            log.error("Failed to send stock alert to chatId={}", chatId, e);
        }
    }
}
