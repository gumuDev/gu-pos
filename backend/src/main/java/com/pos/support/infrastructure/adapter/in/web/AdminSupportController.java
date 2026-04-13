package com.pos.support.infrastructure.adapter.in.web;

import com.pos.support.domain.port.in.ListReportsUseCase;
import com.pos.support.infrastructure.adapter.in.web.dto.SupportReportResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/support")
public class AdminSupportController {

    private static final Logger log = LoggerFactory.getLogger(AdminSupportController.class);

    private final ListReportsUseCase listReportsUseCase;

    public AdminSupportController(ListReportsUseCase listReportsUseCase) {
        this.listReportsUseCase = listReportsUseCase;
    }

    @GetMapping("/reports")
    public ResponseEntity<List<SupportReportResponse>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(
                listReportsUseCase.listAll(page, size).stream()
                        .map(SupportReportResponse::from)
                        .toList()
        );
    }

    @PatchMapping("/reports/{id}/reviewed")
    public ResponseEntity<Void> markReviewed(@PathVariable UUID id) {
        log.info("Marking report as reviewed — id={}", id);
        listReportsUseCase.markReviewed(id);
        return ResponseEntity.noContent().build();
    }
}
