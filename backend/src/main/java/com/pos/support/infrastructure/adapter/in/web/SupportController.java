package com.pos.support.infrastructure.adapter.in.web;

import com.pos.support.domain.port.in.SubmitReportUseCase;
import com.pos.support.infrastructure.adapter.in.web.dto.SubmitReportRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/support")
public class SupportController {

    private static final Logger log = LoggerFactory.getLogger(SupportController.class);

    private final SubmitReportUseCase submitReportUseCase;

    public SupportController(SubmitReportUseCase submitReportUseCase) {
        this.submitReportUseCase = submitReportUseCase;
    }

    @PostMapping("/reports")
    public ResponseEntity<Map<String, String>> submit(@Valid @RequestBody SubmitReportRequest request) {
        log.info("Support report received — tenantId={} type={}", request.tenantId(), request.type());
        submitReportUseCase.submit(request.tenantId(), request.type(), request.description(), request.screenshotUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("status", "received"));
    }
}
