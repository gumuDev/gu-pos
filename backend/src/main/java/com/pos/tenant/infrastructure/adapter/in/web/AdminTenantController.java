package com.pos.tenant.infrastructure.adapter.in.web;

import com.pos.tenant.domain.model.FeatureFlags;
import com.pos.tenant.domain.port.in.GetTenantQuery;
import com.pos.tenant.domain.port.in.GetTenantUseCase;
import com.pos.tenant.domain.port.in.ListTenantsQuery;
import com.pos.tenant.domain.port.in.ListTenantsUseCase;
import com.pos.tenant.domain.port.in.TenantDetail;
import com.pos.tenant.domain.port.in.TenantSummary;
import com.pos.tenant.domain.model.PageResult;
import com.pos.tenant.infrastructure.adapter.in.web.dto.FeatureFlagsResponse;
import com.pos.tenant.infrastructure.adapter.in.web.dto.PagedResponse;
import com.pos.tenant.infrastructure.adapter.in.web.dto.TenantDetailResponse;
import com.pos.tenant.infrastructure.adapter.in.web.dto.TenantSummaryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/tenants")
public class AdminTenantController {

    private static final Logger log = LoggerFactory.getLogger(AdminTenantController.class);

    private final ListTenantsUseCase listTenantsUseCase;
    private final GetTenantUseCase getTenantUseCase;

    public AdminTenantController(ListTenantsUseCase listTenantsUseCase, GetTenantUseCase getTenantUseCase) {
        this.listTenantsUseCase = listTenantsUseCase;
        this.getTenantUseCase = getTenantUseCase;
    }

    @GetMapping
    public ResponseEntity<PagedResponse<TenantSummaryResponse>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Admin list tenants — page={} size={}", page, size);
        PageResult<TenantSummary> result = listTenantsUseCase.list(new ListTenantsQuery(page, size));

        List<TenantSummaryResponse> data = result.data().stream()
                .map(s -> new TenantSummaryResponse(s.id(), s.name(), s.phone(), s.planName(), s.createdAt()))
                .toList();

        return ResponseEntity.ok(new PagedResponse<>(data, result.total(), result.page(), result.size()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TenantDetailResponse> detail(@PathVariable UUID id) {
        log.info("Admin get tenant detail — id={}", id);
        TenantDetail detail = getTenantUseCase.get(new GetTenantQuery(id));
        return ResponseEntity.ok(toResponse(detail));
    }

    private TenantDetailResponse toResponse(TenantDetail d) {
        FeatureFlags f = d.featuresConfig();
        FeatureFlagsResponse flags = new FeatureFlagsResponse(
                f.sizeVariants(), f.modifiers(), f.combos(),
                f.stockControl(), f.ingredientRecipes(), f.customerDisplay(), f.ticketPrinting()
        );
        List<TenantDetailResponse.SubscriptionHistoryItem> history = d.subscriptionHistory().stream()
                .map(h -> new TenantDetailResponse.SubscriptionHistoryItem(
                        h.getId(), h.getPlanId(), h.getStatus(),
                        h.getStartedAt(), h.getEndsAt(), h.getActivatedAt()))
                .toList();
        return new TenantDetailResponse(
                d.id(), d.name(), d.phone(), d.currency(), d.businessType(),
                d.status(), d.createdAt(), flags, d.planId(), d.planName(),
                d.subscriptionStatus(), d.subscriptionStartedAt(), d.subscriptionEndsAt(),
                history, d.branchId(), d.branchName()
        );
    }
}
