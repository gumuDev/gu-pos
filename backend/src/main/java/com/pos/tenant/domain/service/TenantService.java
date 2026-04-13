package com.pos.tenant.domain.service;

import com.pos.tenant.domain.model.Plan;
import com.pos.tenant.domain.model.Tenant;
import com.pos.tenant.domain.port.in.RegisterTenantCommand;
import com.pos.tenant.domain.port.in.RegisterTenantResult;
import com.pos.tenant.domain.port.in.RegisterTenantUseCase;
import com.pos.tenant.domain.port.out.PlanRepository;
import com.pos.tenant.domain.port.out.TenantRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.UUID;

public class TenantService implements RegisterTenantUseCase {

    private static final Logger log = LoggerFactory.getLogger(TenantService.class);

    private final TenantRepository tenantRepository;
    private final PlanRepository planRepository;

    public TenantService(TenantRepository tenantRepository, PlanRepository planRepository) {
        this.tenantRepository = tenantRepository;
        this.planRepository = planRepository;
    }

    @Override
    public RegisterTenantResult register(RegisterTenantCommand command) {
        log.debug("Registering tenant — phone={}", command.phone());

        if (tenantRepository.existsByPhone(command.phone())) {
            log.warn("Registration rejected — phone already registered: {}", command.phone());
            throw new IllegalArgumentException("phone_already_registered");
        }

        Plan localPlan = planRepository.findByName("local")
                .orElseThrow(() -> new IllegalStateException("plan_local_not_found"));

        Tenant tenant = tenantRepository.save(new Tenant(
                UUID.randomUUID(),
                command.businessName(),
                command.phone(),
                command.currency(),
                command.businessType(),
                command.features(),
                localPlan.getId(),
                "active",
                Instant.now()
        ));

        log.info("Tenant created — tenantId={}", tenant.getId());

        return new RegisterTenantResult(tenant.getId(), tenant.getName());
    }
}
