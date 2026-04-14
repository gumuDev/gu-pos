package com.pos.errorlog.domain.port.out;

import com.pos.errorlog.domain.model.MobileErrorLog;

public interface ErrorLogRepository {
    void save(MobileErrorLog log);
}
