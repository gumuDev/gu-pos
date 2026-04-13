package com.pos.tenant.domain.service;

import com.pos.tenant.domain.model.Tenant;
import com.pos.tenant.domain.port.out.StockAlertPort;
import com.pos.tenant.domain.port.out.SubscriptionRepository;
import com.pos.tenant.domain.port.out.TenantRepository;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class StockAlertService {

    private static final int LOW_STOCK_THRESHOLD = 5;
    private static final long ALERT_COOLDOWN_SECONDS = 3600;

    private final TenantRepository tenantRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final StockAlertPort stockAlertPort;
    private final Map<String, Instant> lastAlertSent = new ConcurrentHashMap<>();

    public StockAlertService(TenantRepository tenantRepository,
                             SubscriptionRepository subscriptionRepository,
                             StockAlertPort stockAlertPort) {
        this.tenantRepository = tenantRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.stockAlertPort = stockAlertPort;
    }

    public void checkAndAlert(UUID tenantId, UUID productId, String productName, int currentStock) {
        if (currentStock >= LOW_STOCK_THRESHOLD) {
            return;
        }

        Tenant tenant = tenantRepository.findById(tenantId).orElse(null);
        if (tenant == null) return;

        if (!tenant.getFeaturesConfig().stockControl()) return;
        if (tenant.getTelegramChatId() == null) return;

        boolean hasActivePlan = subscriptionRepository.findActiveByTenantId(tenantId).isPresent();
        if (!hasActivePlan) return;

        String cooldownKey = tenantId + ":" + productId;
        Instant lastAlert = lastAlertSent.get(cooldownKey);
        if (lastAlert != null && Instant.now().isBefore(lastAlert.plusSeconds(ALERT_COOLDOWN_SECONDS))) {
            return;
        }

        stockAlertPort.sendLowStockAlert(tenant.getTelegramChatId(), tenant.getName(), productName, currentStock);
        lastAlertSent.put(cooldownKey, Instant.now());
    }
}
