package com.pos.tenant.domain.port.out;

import com.pos.tenant.domain.model.Branch;

import java.util.Optional;
import java.util.UUID;

public interface BranchRepository {
    Branch save(Branch branch);
    Optional<Branch> findByTenantId(UUID tenantId);
}
