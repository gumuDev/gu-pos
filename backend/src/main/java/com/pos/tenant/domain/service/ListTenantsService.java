package com.pos.tenant.domain.service;

import com.pos.tenant.domain.model.PageResult;
import com.pos.tenant.domain.model.Tenant;
import com.pos.tenant.domain.port.in.ListTenantsQuery;
import com.pos.tenant.domain.port.in.ListTenantsUseCase;
import com.pos.tenant.domain.port.in.TenantSummary;
import com.pos.tenant.domain.port.out.PlanRepository;
import com.pos.tenant.domain.port.out.TenantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ListTenantsService implements ListTenantsUseCase {

    private static final Logger log = LoggerFactory.getLogger(ListTenantsService.class);

    private final TenantRepository tenantRepository;
    private final PlanRepository planRepository;

    public ListTenantsService(TenantRepository tenantRepository, PlanRepository planRepository) {
        this.tenantRepository = tenantRepository;
        this.planRepository = planRepository;
    }

    @Override
    public PageResult<TenantSummary> list(ListTenantsQuery query) {
        log.debug("Listing tenants — page={} size={}", query.page(), query.size());
        PageResult<Tenant> result = tenantRepository.findAll(query.page(), query.size());

        List<TenantSummary> summaries = result.data().stream()
                .map(tenant -> {
                    String planName = planRepository.findById(tenant.getPlanId())
                            .map(p -> p.getName())
                            .orElse("unknown");
                    return new TenantSummary(tenant.getId(), tenant.getName(), tenant.getPhone(), planName, tenant.getCreatedAt());
                })
                .toList();

        return new PageResult<>(summaries, result.total(), result.page(), result.size());
    }
}
