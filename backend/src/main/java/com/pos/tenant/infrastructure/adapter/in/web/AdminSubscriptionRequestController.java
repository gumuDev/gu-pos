package com.pos.tenant.infrastructure.adapter.in.web;

import com.pos.tenant.domain.model.PageResult;
import com.pos.tenant.domain.model.SubscriptionRequest;
import com.pos.tenant.domain.port.in.ListSubscriptionRequestsUseCase;
import com.pos.tenant.domain.port.in.ListTenantsQuery;
import com.pos.tenant.infrastructure.adapter.in.web.dto.PagedResponse;
import com.pos.tenant.infrastructure.adapter.in.web.dto.SubscriptionRequestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/subscription-requests")
public class AdminSubscriptionRequestController {

    private static final Logger log = LoggerFactory.getLogger(AdminSubscriptionRequestController.class);

    private final ListSubscriptionRequestsUseCase listUseCase;

    public AdminSubscriptionRequestController(ListSubscriptionRequestsUseCase listUseCase) {
        this.listUseCase = listUseCase;
    }

    @GetMapping
    public ResponseEntity<PagedResponse<SubscriptionRequestResponse>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Admin list subscription requests — page={} size={}", page, size);
        PageResult<SubscriptionRequest> result = listUseCase.list(new ListTenantsQuery(page, size));

        List<SubscriptionRequestResponse> data = result.data().stream()
                .map(r -> new SubscriptionRequestResponse(
                        r.getId(), r.getTenantId(), r.getPlanName(),
                        r.getTransactionRef(), r.getReceiptUrl(),
                        r.getStatus(), r.getCreatedAt()))
                .toList();

        return ResponseEntity.ok(new PagedResponse<>(data, result.total(), result.page(), result.size()));
    }
}
