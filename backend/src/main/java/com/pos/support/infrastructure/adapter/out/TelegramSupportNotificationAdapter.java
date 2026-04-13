package com.pos.support.infrastructure.adapter.out;

import com.pos.support.domain.model.SupportReport;
import com.pos.support.domain.port.out.SupportNotificationPort;
import com.pos.tenant.infrastructure.adapter.in.telegram.AdminTelegramBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TelegramSupportNotificationAdapter implements SupportNotificationPort {

    private static final Logger log = LoggerFactory.getLogger(TelegramSupportNotificationAdapter.class);

    private final AdminTelegramBot bot;
    private final long adminChatId;

    public TelegramSupportNotificationAdapter(AdminTelegramBot bot, long adminChatId) {
        this.bot = bot;
        this.adminChatId = adminChatId;
    }

    @Override
    public void notifyNewReport(SupportReport report, String tenantName) {
        String icon = report.getType().equals("bug") ? "🐞" : "💡";
        String label = report.getType().equals("bug") ? "Nuevo problema reportado" : "Nueva sugerencia";
        String text = String.format(
                "%s <b>%s</b>\n\n" +
                "👤 Negocio: %s\n" +
                "📝 %s\n" +
                "🪪 TenantID: <code>%s</code>",
                icon, label,
                tenantName,
                report.getDescription(),
                report.getTenantId()
        );
        if (report.getScreenshotUrl() != null && !report.getScreenshotUrl().isBlank()) {
            text += String.format("\n🖼 <a href=\"%s\">Ver captura</a>", report.getScreenshotUrl());
        }
        log.info("Sending support notification to admin — type={} tenant={}", report.getType(), tenantName);
        bot.sendTextMessage(adminChatId, text);
    }
}
