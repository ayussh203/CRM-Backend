package com.crm.server.service;

import com.crm.server.config.TenantContext;
import com.crm.server.dto.WorkflowDTO;
import com.crm.server.entity.Tenant;
import com.crm.server.entity.Workflow;
import com.crm.server.repository.TenantRepository;
import com.crm.server.repository.WorkflowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkflowService {

    private final WorkflowRepository workflowRepository;
    private final TenantRepository tenantRepository;

    public Workflow createWorkflow(WorkflowDTO dto) {
        String tenantId = TenantContext.getTenantId();
        Tenant tenant = tenantRepository.findById(UUID.fromString(tenantId))
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        Workflow workflow = new Workflow(
            dto.name(),
            dto.triggerEvent(),
            dto.conditions(),
            dto.actions(),
            tenant
        );
        return workflowRepository.save(workflow);
    }
}