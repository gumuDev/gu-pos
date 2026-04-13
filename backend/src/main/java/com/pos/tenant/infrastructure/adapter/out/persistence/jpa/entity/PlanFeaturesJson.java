package com.pos.tenant.infrastructure.adapter.out.persistence.jpa.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlanFeaturesJson {

    @JsonProperty("sync")
    private boolean sync;

    @JsonProperty("advancedReports")
    private boolean advancedReports;

    @JsonProperty("maxCashiers")
    private int maxCashiers;

    @JsonProperty("maxBranches")
    private Integer maxBranches;

    @JsonProperty("multiBranch")
    private boolean multiBranch;

    @JsonProperty("telegramStockAlerts")
    private boolean telegramStockAlerts;

    @JsonProperty("csvExport")
    private boolean csvExport;

    public boolean isSync() { return sync; }
    public boolean isAdvancedReports() { return advancedReports; }
    public int getMaxCashiers() { return maxCashiers; }
    public Integer getMaxBranches() { return maxBranches; }
    public boolean isMultiBranch() { return multiBranch; }
    public boolean isTelegramStockAlerts() { return telegramStockAlerts; }
    public boolean isCsvExport() { return csvExport; }
}
