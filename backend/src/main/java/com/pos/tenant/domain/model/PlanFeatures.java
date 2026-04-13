package com.pos.tenant.domain.model;

public record PlanFeatures(
        boolean sync,
        boolean advancedReports,
        int maxCashiers,
        Integer maxBranches,
        boolean multiBranch,
        boolean telegramStockAlerts,
        boolean csvExport
) {
    public static PlanFeatures free() {
        return new PlanFeatures(false, false, 2, 1, false, false, false);
    }
}
