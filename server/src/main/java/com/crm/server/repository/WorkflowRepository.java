package com.crm.server.repository;

import com.crm.server.entity.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface WorkflowRepository extends JpaRepository<Workflow, UUID> {
    List<Workflow> findAllByTenantIdAndTriggerEventAndIsActiveTrue(UUID tenantId, String triggerEvent);
}