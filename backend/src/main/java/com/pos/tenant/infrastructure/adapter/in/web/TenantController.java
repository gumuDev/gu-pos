package com.pos.tenant.infrastructure.adapter.in.web;

import com.pos.tenant.domain.model.FeatureFlags;
import com.pos.tenant.domain.port.in.GetPlanFeaturesUseCase;
import com.pos.tenant.domain.port.in.PlanFeaturesResult;
import com.pos.tenant.domain.port.in.RegisterTenantCommand;
import com.pos.tenant.domain.port.in.RegisterTenantResult;
import com.pos.tenant.domain.port.in.RegisterTenantUseCase;
import com.pos.tenant.domain.port.out.TenantRepository;
import com.pos.tenant.infrastructure.adapter.in.web.dto.PlanFeaturesResponse;
import com.pos.tenant.infrastructure.adapter.in.web.dto.RegisterTenantRequest;
import com.pos.tenant.infrastructure.adapter.in.web.dto.RegisterTenantResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tenant")
public class TenantController {

    private static final Logger log = LoggerFactory.getLogger(TenantController.class);

    private final RegisterTenantUseCase registerTenantUseCase;
    private final GetPlanFeaturesUseCase getPlanFeaturesUseCase;
    private final TenantRepository tenantRepository;

    public TenantController(RegisterTenantUseCase registerTenantUseCase,
                            GetPlanFeaturesUseCase getPlanFeaturesUseCase,
                            TenantRepository tenantRepository) {
        this.registerTenantUseCase = registerTenantUseCase;
        this.getPlanFeaturesUseCase = getPlanFeaturesUseCase;
        this.tenantRepository = tenantRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterTenantResponse> register(@Valid @RequestBody RegisterTenantRequest request) {
        log.info("Tenant registration request — phone={} businessName={}", request.phone(), request.businessName());
        RegisterTenantCommand command = new RegisterTenantCommand(
                request.businessName(),
                request.phone(),
                request.currency(),
                request.businessType(),
                new FeatureFlags(
                        request.features().sizeVariants(),
                        request.features().modifiers(),
                        request.features().combos(),
                        request.features().stockControl(),
                        request.features().ingredientRecipes(),
                        request.features().customerDisplay(),
                        request.features().ticketPrinting()
                )
        );

        RegisterTenantResult result = registerTenantUseCase.register(command);
        log.info("Tenant registered — tenantId={}", result.tenantId());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new RegisterTenantResponse(result.tenantId(), result.businessName())
        );
    }

    @GetMapping("/by-phone/{phone}")
    public ResponseEntity<RegisterTenantResponse> getByPhone(@PathVariable String phone) {
        return tenantRepository.findByPhone(phone)
                .map(t -> ResponseEntity.ok(new RegisterTenantResponse(t.getId(), t.getName())))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{tenantId}/telegram")
    public ResponseEntity<java.util.Map<String, Boolean>> getTelegramStatus(@PathVariable UUID tenantId) {
        boolean configured = tenantRepository.findById(tenantId)
                .map(t -> t.getTelegramChatId() != null)
                .orElse(false);
        return ResponseEntity.ok(java.util.Map.of("configured", configured));
    }

    @DeleteMapping("/{tenantId}/telegram")
    public ResponseEntity<Void> clearTelegram(@PathVariable UUID tenantId) {
        tenantRepository.clearTelegramChatId(tenantId);
        log.info("Telegram chatId cleared — tenantId={}", tenantId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{tenantId}/plan")
    public ResponseEntity<PlanFeaturesResponse> getPlan(@PathVariable UUID tenantId) {
        log.info("Get plan features — tenantId={}", tenantId);
        PlanFeaturesResult result = getPlanFeaturesUseCase.getByTenantId(tenantId);
        return ResponseEntity.ok(new PlanFeaturesResponse(
                result.planName(),
                result.active(),
                result.endsAt(),
                result.features().sync(),
                result.features().advancedReports(),
                result.features().maxCashiers(),
                result.features().maxBranches(),
                result.features().multiBranch(),
                result.features().telegramStockAlerts(),
                result.features().csvExport(),
                result.branchId()
        ));
    }
}
