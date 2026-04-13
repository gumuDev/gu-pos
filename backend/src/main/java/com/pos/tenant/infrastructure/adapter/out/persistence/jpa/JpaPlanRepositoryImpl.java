package com.pos.tenant.infrastructure.adapter.out.persistence.jpa;

import com.pos.tenant.domain.model.Plan;
import com.pos.tenant.domain.model.PlanFeatures;
import com.pos.tenant.domain.port.out.PlanRepository;
import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.entity.PlanEntity;
import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.entity.PlanFeaturesJson;
import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.repository.JpaPlanRepository;

import java.util.Optional;
import java.util.UUID;

public class JpaPlanRepositoryImpl implements PlanRepository {

    private final JpaPlanRepository jpa;

    public JpaPlanRepositoryImpl(JpaPlanRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<Plan> findByName(String name) {
        return jpa.findByName(name).map(this::toDomain);
    }

    @Override
    public Optional<Plan> findById(UUID id) {
        return jpa.findById(id).map(this::toDomain);
    }

    private Plan toDomain(PlanEntity e) {
        return new Plan(e.getId(), e.getName(), toFeatures(e.getFeatures()));
    }

    private PlanFeatures toFeatures(PlanFeaturesJson json) {
        if (json == null) return PlanFeatures.free();
        return new PlanFeatures(
                json.isSync(),
                json.isAdvancedReports(),
                json.getMaxCashiers(),
                json.getMaxBranches(),
                json.isMultiBranch(),
                json.isTelegramStockAlerts(),
                json.isCsvExport()
        );
    }
}
