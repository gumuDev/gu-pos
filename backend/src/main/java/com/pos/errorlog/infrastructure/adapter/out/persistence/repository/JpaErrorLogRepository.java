package com.pos.errorlog.infrastructure.adapter.out.persistence.repository;

import com.pos.errorlog.infrastructure.adapter.out.persistence.entity.MobileErrorLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaErrorLogRepository extends JpaRepository<MobileErrorLogEntity, UUID> {}
