package com.crm.server.repository;

import com.crm.server.entity.Lead;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface LeadRepository extends JpaRepository<Lead, UUID> {
    // Allows fetching leads ONLY for a specific tenant
    List<Lead> findAllByTenantId(UUID tenantId);
}