package com.pos.tenant.domain.port.out;

import com.pos.tenant.domain.model.SubscriptionRequest;

public interface AdminNotificationPort {
    void notifyNewPaymentRequest(SubscriptionRequest request, String tenantName);
    void notifyActivationResult(String tenantPhone, boolean approved, String planName);
}
