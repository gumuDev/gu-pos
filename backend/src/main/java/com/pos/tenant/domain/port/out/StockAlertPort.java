package com.pos.tenant.domain.port.out;

public interface StockAlertPort {
    void sendLowStockAlert(Long chatId, String businessName, String productName, int currentStock);
}
