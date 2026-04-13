package com.pos.tenant.infrastructure.adapter.out.persistence.memory;

import com.pos.tenant.domain.model.Branch;
import com.pos.tenant.domain.port.out.BranchRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryBranchRepository implements BranchRepository {

    private final Map<UUID, Branch> store = new HashMap<>();

    @Override
    public Branch save(Branch branch) {
        store.put(branch.getId(), branch);
        return branch;
    }

    @Override
    public Optional<Branch> findByTenantId(UUID tenantId) {
        return store.values().stream()
                .filter(b -> b.getTenantId().equals(tenantId))
                .findFirst();
    }
}
