package com.pos.tenant.infrastructure.adapter.in.web;

import com.pos.tenant.domain.model.PageResult;
import com.pos.tenant.domain.port.in.ActivateSubscriptionCommand;
import com.pos.tenant.domain.port.in.ActivateSubscriptionUseCase;
import com.pos.tenant.domain.port.in.CancelSubscriptionUseCase;
import com.pos.tenant.domain.port.in.ListSubscriptionsUseCase;
import com.pos.tenant.domain.port.in.ListTenantsQuery;
import com.pos.tenant.domain.port.in.SubscriptionSummary;
import com.pos.tenant.infrastructure.adapter.in.web.dto.PagedResponse;
import com.pos.tenant.infrastructure.adapter.in.web.dto.SubscriptionSummaryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/subscriptions")
public class AdminSubscriptionController {

    private static final Logger log = LoggerFactory.getLogger(AdminSubscriptionController.class);

    private final ListSubscriptionsUseCase listSubscriptionsUseCase;
    private final ActivateSubscriptionUseCase activateSubscriptionUseCase;
    private final CancelSubscriptionUseCase cancelSubscriptionUseCase;

    public AdminSubscriptionController(ListSubscriptionsUseCase listSubscriptionsUseCase,
                                       ActivateSubscriptionUseCase activateSubscriptionUseCase,
                                       CancelSubscriptionUseCase cancelSubscriptionUseCase) {
        this.listSubscriptionsUseCase = listSubscriptionsUseCase;
        this.activateSubscriptionUseCase = activateSubscriptionUseCase;
        this.cancelSubscriptionUseCase = cancelSubscriptionUseCase;
    }

    @GetMapping
    public ResponseEntity<PagedResponse<SubscriptionSummaryResponse>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Admin list subscriptions — page={} size={}", page, size);
        PageResult<SubscriptionSummary> result = listSubscriptionsUseCase.list(new ListTenantsQuery(page, size));

        List<SubscriptionSummaryResponse> data = result.data().stream()
                .map(s -> new SubscriptionSummaryResponse(
                        s.tenantId(), s.tenantName(), s.tenantPhone(),
                        s.planName(), s.subscriptionStatus(), s.subscriptionEndsAt()))
                .toList();

        return ResponseEntity.ok(new PagedResponse<>(data, result.total(), result.page(), result.size()));
    }

    @PostMapping("/{tenantId}/activate")
    public ResponseEntity<Void> activate(@PathVariable UUID tenantId,
                                         @RequestBody ActivateRequest body) {
        log.info("Admin activate subscription — tenantId={} plan={}", tenantId, body.planName());
        activateSubscriptionUseCase.activate(new ActivateSubscriptionCommand(tenantId, body.planName()));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{tenantId}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable UUID tenantId) {
        log.info("Admin cancel subscription — tenantId={}", tenantId);
        cancelSubscriptionUseCase.cancel(tenantId);
        return ResponseEntity.ok().build();
    }

    public record ActivateRequest(String planName) {}
}
