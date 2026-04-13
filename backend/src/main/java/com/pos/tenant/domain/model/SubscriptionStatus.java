package com.pos.tenant.domain.model;

public enum SubscriptionStatus {
    ACTIVE,
    EXPIRED,
    CANCELED,
    SUSPENDED,
    PAST_DUE;

    public String toValue() {
        return name().toLowerCase();
    }

    public static SubscriptionStatus from(String value) {
        return valueOf(value.toUpperCase());
    }
}
