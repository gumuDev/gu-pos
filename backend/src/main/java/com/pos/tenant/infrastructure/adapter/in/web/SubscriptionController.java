package com.pos.tenant.infrastructure.adapter.in.web;

import com.pos.tenant.domain.port.in.RequestSubscriptionCommand;
import com.pos.tenant.domain.port.in.RequestSubscriptionUseCase;
import com.pos.tenant.domain.port.out.SubscriptionRequestRepository;
import com.pos.tenant.infrastructure.adapter.in.web.dto.RequestSubscriptionRequest;
import com.pos.tenant.infrastructure.adapter.in.web.dto.SubscriptionRequestResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/subscription")
public class SubscriptionController {

    private static final Logger log = LoggerFactory.getLogger(SubscriptionController.class);

    private final RequestSubscriptionUseCase requestSubscriptionUseCase;
    private final SubscriptionRequestRepository subscriptionRequestRepository;

    public SubscriptionController(RequestSubscriptionUseCase requestSubscriptionUseCase,
                                  SubscriptionRequestRepository subscriptionRequestRepository) {
        this.requestSubscriptionUseCase = requestSubscriptionUseCase;
        this.subscriptionRequestRepository = subscriptionRequestRepository;
    }

    @PostMapping("/request")
    public ResponseEntity<Map<String, UUID>> requestSubscription(@Valid @RequestBody RequestSubscriptionRequest request) {
        log.info("Subscription request received — tenantId={} plan={}", request.tenantId(), request.planName());
        UUID requestId = requestSubscriptionUseCase.request(new RequestSubscriptionCommand(
                request.tenantId(),
                request.planName(),
                request.transactionRef(),
                request.receiptUrl()
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("requestId", requestId));
    }

    @GetMapping("/request/latest")
    public ResponseEntity<SubscriptionRequestResponse> getLatestRequest(@RequestParam UUID tenantId) {
        return subscriptionRequestRepository.findLatestByTenantId(tenantId)
                .map(r -> ResponseEntity.ok(new SubscriptionRequestResponse(
                        r.getId(), r.getTenantId(), r.getPlanName(),
                        r.getTransactionRef(), r.getReceiptUrl(),
                        r.getStatus(), r.getCreatedAt()
                )))
                .orElse(ResponseEntity.noContent().build());
    }
}
