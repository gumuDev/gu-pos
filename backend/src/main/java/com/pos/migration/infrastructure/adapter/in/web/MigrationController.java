package com.pos.migration.infrastructure.adapter.in.web;

import com.pos.migration.domain.service.MigrationService;
import com.pos.migration.infrastructure.adapter.in.web.dto.MigrateCatalogRequest;
import com.pos.migration.infrastructure.adapter.in.web.dto.MigrateOrdersRequest;
import com.pos.migration.infrastructure.adapter.in.web.dto.MigrateStockRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/migration")
public class MigrationController {

    private static final Logger log = LoggerFactory.getLogger(MigrationController.class);

    private final MigrationService migrationService;

    public MigrationController(MigrationService migrationService) {
        this.migrationService = migrationService;
    }

    @PostMapping("/catalog")
    public ResponseEntity<Map<String, String>> catalog(@RequestBody MigrateCatalogRequest request) {
        log.info("Migration catalog request — tenantId={}", request.tenantId());
        migrationService.migrateCatalog(request);
        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    @PostMapping("/orders")
    public ResponseEntity<Map<String, String>> orders(@RequestBody MigrateOrdersRequest request) {
        log.info("Migration orders request — tenantId={}", request.tenantId());
        migrationService.migrateOrders(request);
        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    @PostMapping("/stock")
    public ResponseEntity<Map<String, String>> stock(@RequestBody MigrateStockRequest request) {
        log.info("Migration stock request — tenantId={}", request.tenantId());
        migrationService.migrateStock(request);
        return ResponseEntity.ok(Map.of("status", "ok"));
    }
}
