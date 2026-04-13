package com.pos.tenant.domain.service;

import com.pos.tenant.domain.model.SubscriptionRequest;
import com.pos.tenant.domain.model.Tenant;
import com.pos.tenant.domain.port.in.RequestSubscriptionCommand;
import com.pos.tenant.domain.port.in.RequestSubscriptionUseCase;
import com.pos.tenant.domain.port.out.AdminNotificationPort;
import com.pos.tenant.domain.port.out.SubscriptionRequestRepository;
import com.pos.tenant.domain.port.out.TenantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.UUID;

public class RequestSubscriptionService implements RequestSubscriptionUseCase {

    private static final Logger log = LoggerFactory.getLogger(RequestSubscriptionService.class);

    private final SubscriptionRequestRepository requestRepository;
    private final TenantRepository tenantRepository;
    private final AdminNotificationPort adminNotification;

    public RequestSubscriptionService(SubscriptionRequestRepository requestRepository,
                                      TenantRepository tenantRepository,
                                      AdminNotificationPort adminNotification) {
        this.requestRepository = requestRepository;
        this.tenantRepository = tenantRepository;
        this.adminNotification = adminNotification;
    }

    @Override
    public UUID request(RequestSubscriptionCommand command) {
        log.info("Subscription request — tenantId={} plan={}", command.tenantId(), command.planName());

        Tenant tenant = tenantRepository.findById(command.tenantId())
                .orElseThrow(() -> new IllegalArgumentException("tenant_not_found"));

        SubscriptionRequest request = new SubscriptionRequest(
                UUID.randomUUID(),
                command.tenantId(),
                command.planName(),
                command.transactionRef(),
                command.receiptUrl(),
                "pending",
                Instant.now()
        );

        requestRepository.save(request);
        adminNotification.notifyNewPaymentRequest(request, tenant.getName());

        log.info("Subscription request saved and admin notified — requestId={}", request.getId());
        return request.getId();
    }
}
