package com.pos.tenant.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.tenant.domain.port.in.ActivateSubscriptionUseCase;
import com.pos.tenant.domain.port.in.CancelSubscriptionUseCase;
import com.pos.tenant.domain.port.in.GetPlanFeaturesUseCase;
import com.pos.tenant.domain.port.in.GetTenantUseCase;
import com.pos.tenant.domain.port.in.ListSubscriptionRequestsUseCase;
import com.pos.tenant.domain.port.in.ListSubscriptionsUseCase;
import com.pos.tenant.domain.port.in.ListTenantsUseCase;
import com.pos.tenant.domain.port.in.RequestSubscriptionUseCase;
import com.pos.tenant.domain.port.out.AdminNotificationPort;
import com.pos.tenant.domain.port.out.BranchRepository;
import com.pos.tenant.domain.port.out.PlanRepository;
import com.pos.tenant.domain.port.out.SubscriptionRepository;
import com.pos.tenant.domain.port.out.SubscriptionRequestRepository;
import com.pos.tenant.domain.port.out.TenantRepository;
import com.pos.tenant.domain.port.out.UserRepository;
import com.pos.tenant.domain.service.ActivateSubscriptionService;
import com.pos.tenant.domain.service.CancelSubscriptionService;
import com.pos.tenant.domain.service.GetPlanFeaturesService;
import com.pos.tenant.domain.service.GetTenantService;
import com.pos.tenant.domain.service.ListSubscriptionRequestsService;
import com.pos.tenant.domain.service.ListSubscriptionsService;
import com.pos.tenant.domain.service.ListTenantsService;
import com.pos.tenant.domain.service.RequestSubscriptionService;
import com.pos.tenant.domain.port.out.StockAlertPort;
import com.pos.tenant.domain.service.PlanGuard;
import com.pos.tenant.domain.service.StockAlertService;
import com.pos.tenant.domain.service.TenantService;
import com.pos.tenant.infrastructure.job.SubscriptionDowngradeJob;
import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.JpaBranchRepositoryImpl;
import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.JpaPlanRepositoryImpl;
import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.JpaSubscriptionRepositoryImpl;
import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.JpaSubscriptionRequestRepositoryImpl;
import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.JpaTenantRepositoryImpl;
import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.JpaUserRepositoryImpl;
import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.repository.JpaBranchRepository;
import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.repository.JpaPlanRepository;
import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.repository.JpaSubscriptionHistoryRepository;
import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.repository.JpaSubscriptionRepository;
import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.repository.JpaSubscriptionRequestRepository;
import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.repository.JpaTenantRepository;
import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.repository.JpaUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"local", "production"})
public class TenantJpaBeanConfig {

    @Bean
    public PlanRepository planRepository(JpaPlanRepository jpa) {
        return new JpaPlanRepositoryImpl(jpa);
    }

    @Bean
    public TenantRepository tenantRepository(JpaTenantRepository jpa, ObjectMapper objectMapper) {
        return new JpaTenantRepositoryImpl(jpa, objectMapper);
    }

    @Bean
    public SubscriptionRepository subscriptionRepository(JpaSubscriptionRepository jpa,
                                                          JpaSubscriptionHistoryRepository jpaHistory) {
        return new JpaSubscriptionRepositoryImpl(jpa, jpaHistory);
    }

    @Bean
    public UserRepository userRepository(JpaUserRepository jpa) {
        return new JpaUserRepositoryImpl(jpa);
    }

    @Bean
    public BranchRepository branchRepository(JpaBranchRepository jpa) {
        return new JpaBranchRepositoryImpl(jpa);
    }

    @Bean
    public TenantService tenantService(TenantRepository tenantRepository, PlanRepository planRepository) {
        return new TenantService(tenantRepository, planRepository);
    }

    @Bean
    public ListTenantsUseCase listTenantsUseCase(TenantRepository tenantRepository,
                                                 PlanRepository planRepository) {
        return new ListTenantsService(tenantRepository, planRepository);
    }

    @Bean
    public ListSubscriptionsUseCase listSubscriptionsUseCase(TenantRepository tenantRepository,
                                                              PlanRepository planRepository,
                                                              SubscriptionRepository subscriptionRepository) {
        return new ListSubscriptionsService(tenantRepository, planRepository, subscriptionRepository);
    }

    @Bean
    public GetTenantUseCase getTenantUseCase(TenantRepository tenantRepository,
                                             PlanRepository planRepository,
                                             SubscriptionRepository subscriptionRepository,
                                             BranchRepository branchRepository) {
        return new GetTenantService(tenantRepository, planRepository, subscriptionRepository, branchRepository);
    }

    @Bean
    public GetPlanFeaturesUseCase getPlanFeaturesUseCase(SubscriptionRepository subscriptionRepository,
                                                         PlanRepository planRepository,
                                                         BranchRepository branchRepository) {
        return new GetPlanFeaturesService(subscriptionRepository, planRepository, branchRepository);
    }

    @Bean
    public SubscriptionRequestRepository subscriptionRequestRepository(JpaSubscriptionRequestRepository jpa) {
        return new JpaSubscriptionRequestRepositoryImpl(jpa);
    }

    @Bean
    public ListSubscriptionRequestsUseCase listSubscriptionRequestsUseCase(
            SubscriptionRequestRepository subscriptionRequestRepository) {
        return new ListSubscriptionRequestsService(subscriptionRequestRepository);
    }

    @Bean
    public ActivateSubscriptionUseCase activateSubscriptionUseCase(SubscriptionRepository subscriptionRepository,
                                                                    SubscriptionRequestRepository requestRepository,
                                                                    PlanRepository planRepository,
                                                                    TenantRepository tenantRepository,
                                                                    BranchRepository branchRepository) {
        return new ActivateSubscriptionService(subscriptionRepository, requestRepository,
                planRepository, tenantRepository, branchRepository);
    }

    @Bean
    public CancelSubscriptionUseCase cancelSubscriptionUseCase(SubscriptionRepository subscriptionRepository) {
        return new CancelSubscriptionService(subscriptionRepository);
    }

    @Bean
    public RequestSubscriptionUseCase requestSubscriptionUseCase(SubscriptionRequestRepository requestRepository,
                                                                  TenantRepository tenantRepository,
                                                                  AdminNotificationPort adminNotificationPort) {
        return new RequestSubscriptionService(requestRepository, tenantRepository, adminNotificationPort);
    }

    @Bean
    public PlanGuard planGuard(GetPlanFeaturesUseCase getPlanFeaturesUseCase) {
        return new PlanGuard(getPlanFeaturesUseCase);
    }

    @Bean
    public SubscriptionDowngradeJob subscriptionDowngradeJob(SubscriptionRepository subscriptionRepository,
                                                              PlanRepository planRepository) {
        return new SubscriptionDowngradeJob(subscriptionRepository, planRepository);
    }

    @Bean
    public StockAlertService stockAlertService(TenantRepository tenantRepository,
                                               SubscriptionRepository subscriptionRepository,
                                               StockAlertPort stockAlertPort) {
        return new StockAlertService(tenantRepository, subscriptionRepository, stockAlertPort);
    }

    @Bean
    public com.pos.tenant.domain.port.out.DeviceRepository deviceRepository(
            com.pos.tenant.infrastructure.adapter.out.persistence.jpa.repository.JpaDeviceRepository jpa) {
        return new com.pos.tenant.infrastructure.adapter.out.persistence.jpa.JpaDeviceRepositoryImpl(jpa);
    }

    @Bean
    public com.pos.tenant.domain.port.out.SessionRepository sessionRepository(
            com.pos.tenant.infrastructure.adapter.out.persistence.jpa.repository.JpaSessionRepository jpa) {
        return new com.pos.tenant.infrastructure.adapter.out.persistence.jpa.JpaSessionRepositoryImpl(jpa);
    }

    @Bean
    public com.pos.tenant.domain.service.AuthService authService(
            UserRepository userRepository,
            com.pos.tenant.domain.port.out.DeviceRepository deviceRepository,
            com.pos.tenant.domain.port.out.SessionRepository sessionRepository,
            com.pos.tenant.domain.port.out.AuthTokenPort authTokenPort) {
        return new com.pos.tenant.domain.service.AuthService(
                userRepository, deviceRepository, sessionRepository, authTokenPort);
    }

    @Bean
    public com.pos.tenant.domain.port.in.LoginUseCase loginUseCase(com.pos.tenant.domain.service.AuthService authService) {
        return authService;
    }

    @Bean
    public com.pos.tenant.domain.port.in.RefreshTokenUseCase refreshTokenUseCase(com.pos.tenant.domain.service.AuthService authService) {
        return authService;
    }

    @Bean
    public com.pos.tenant.domain.port.in.RegisterAccountUseCase registerAccountUseCase(
            TenantRepository tenantRepository,
            UserRepository userRepository,
            SubscriptionRepository subscriptionRepository,
            com.pos.tenant.domain.port.out.DeviceRepository deviceRepository,
            com.pos.tenant.domain.port.out.SessionRepository sessionRepository,
            com.pos.tenant.domain.port.out.AuthTokenPort authTokenPort) {
        return new com.pos.tenant.domain.service.RegisterAccountService(
                tenantRepository, userRepository, subscriptionRepository,
                deviceRepository, sessionRepository, authTokenPort);
    }
}
