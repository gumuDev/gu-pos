package com.pos.support.domain.port.out;

import com.pos.support.domain.model.SupportReport;

public interface SupportNotificationPort {
    void notifyNewReport(SupportReport report, String tenantName);
}
