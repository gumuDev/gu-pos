package com.pos.errorlog.infrastructure.adapter.in.web;

import com.pos.errorlog.domain.model.ErrorLevel;
import com.pos.errorlog.domain.port.in.CreateErrorLogUseCase;
import com.pos.errorlog.infrastructure.adapter.in.web.dto.CreateErrorLogRequest;
import com.pos.errorlog.infrastructure.adapter.in.web.dto.CreateErrorLogResponse;
import com.pos.tenant.infrastructure.security.JwtAuthenticationToken;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/mobile")
public class ErrorLogController {

    private static final Logger log = LoggerFactory.getLogger(ErrorLogController.class);

    private final CreateErrorLogUseCase createErrorLogUseCase;

    public ErrorLogController(CreateErrorLogUseCase createErrorLogUseCase) {
        this.createErrorLogUseCase = createErrorLogUseCase;
    }

    @PostMapping("/error-logs")
    public ResponseEntity<CreateErrorLogResponse> create(@Valid @RequestBody CreateErrorLogRequest request) {
        UUID tenantId = null;
        UUID userId = null;

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken jwt) {
            tenantId = jwt.getTenantId();
            userId = jwt.getUserId();
        }

        ErrorLevel level;
        try {
            level = ErrorLevel.valueOf(request.level());
        } catch (IllegalArgumentException e) {
            level = ErrorLevel.error;
        }

        UUID errorId = createErrorLogUseCase.create(
            tenantId, userId, request.sessionId(), level,
            request.message(), request.stack(), request.screen(), request.action(),
            request.platform(), request.osVersion(), request.deviceModel(),
            request.appVersion(), request.buildNumber(), request.occurredAt()
        );

        log.info("Mobile error log stored — errorId={} level={} screen={} tenantId={}",
            errorId, level, request.screen(), tenantId);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new CreateErrorLogResponse(errorId, true));
    }
}
