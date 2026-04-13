package com.pos.tenant.infrastructure.adapter.out.persistence.jpa;

import com.pos.tenant.domain.model.Branch;
import com.pos.tenant.domain.port.out.BranchRepository;
import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.entity.BranchEntity;
import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.repository.JpaBranchRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class JpaBranchRepositoryImpl implements BranchRepository {

    private final JpaBranchRepository jpa;

    public JpaBranchRepositoryImpl(JpaBranchRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Branch save(Branch branch) {
        BranchEntity entity = new BranchEntity();
        entity.setId(branch.getId());
        entity.setTenantId(branch.getTenantId());
        entity.setName(branch.getName());
        entity.setActive(true);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        jpa.save(entity);
        return branch;
    }

    @Override
    public Optional<Branch> findByTenantId(UUID tenantId) {
        return jpa.findByTenantId(tenantId)
                .map(e -> new Branch(e.getId(), e.getTenantId(), e.getName()));
    }
}
