package com.crm.server.repository;

import com.crm.server.entity.Lead;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LeadRepository extends JpaRepository<Lead, UUID> {
    
    Page<Lead> findAllByTenantId(UUID tenantId, Pageable pageable);
}