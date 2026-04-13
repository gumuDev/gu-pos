package com.pos.tenant.domain.service;

import com.pos.tenant.domain.model.PlanFeatures;
import com.pos.tenant.domain.port.in.GetPlanFeaturesUseCase;
import com.pos.tenant.domain.port.in.PlanFeaturesResult;

import java.util.UUID;

public class PlanGuard {

    private final GetPlanFeaturesUseCase getPlanFeaturesUseCase;

    public PlanGuard(GetPlanFeaturesUseCase getPlanFeaturesUseCase) {
        this.getPlanFeaturesUseCase = getPlanFeaturesUseCase;
    }

    public void requireAdvancedReports(UUID tenantId) {
        PlanFeatures features = getPlanFeaturesUseCase.getByTenantId(tenantId).features();
        if (!features.advancedReports()) {
            throw new PlanLimitException("advanced_reports_not_included_in_plan");
        }
    }

    public void requireCsvExport(UUID tenantId) {
        PlanFeatures features = getPlanFeaturesUseCase.getByTenantId(tenantId).features();
        if (!features.csvExport()) {
            throw new PlanLimitException("csv_export_not_included_in_plan");
        }
    }

    public void requireMultiBranch(UUID tenantId) {
        PlanFeatures features = getPlanFeaturesUseCase.getByTenantId(tenantId).features();
        if (!features.multiBranch()) {
            throw new PlanLimitException("multi_branch_not_included_in_plan");
        }
    }

    public void checkCashierLimit(UUID tenantId, int currentCashierCount) {
        PlanFeaturesResult result = getPlanFeaturesUseCase.getByTenantId(tenantId);
        int maxCashiers = result.features().maxCashiers();
        if (currentCashierCount >= maxCashiers) {
            throw new PlanLimitException("cashier_limit_reached:" + maxCashiers);
        }
    }
}
