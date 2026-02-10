package com.crm.server.service;

import com.crm.server.config.TenantContext;
import com.crm.server.dto.LeadDTO;
import com.crm.server.entity.Lead;
import com.crm.server.entity.Tenant;
import com.crm.server.repository.LeadRepository;
import com.crm.server.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.crm.server.event.LeadCreatedEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LeadService {

    private final LeadRepository leadRepository;
    private final TenantRepository tenantRepository;
    private final EventProducer eventProducer;

    public Lead createLead(LeadDTO dto) {
        // 1. FETCH TENANT ID FROM SECURITY CONTEXT
        String tenantId = TenantContext.getTenantId();
        
        Tenant tenant = tenantRepository.findById(UUID.fromString(tenantId))
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        
        Lead lead = new Lead();
        lead.setFirstName(dto.getFirstName());
        lead.setLastName(dto.getLastName());
        lead.setEmail(dto.getEmail());
        lead.setCompany(dto.getCompany());
        lead.setSource(dto.getSource());
        lead.setTenant(tenant); 
Lead savedLead = leadRepository.save(lead);
LeadCreatedEvent event = new LeadCreatedEvent(
                savedLead.getId(),
                savedLead.getTenant().getId(),
                savedLead.getEmail()
        );
        eventProducer.publishLeadCreated(event);
     //   return leadRepository.save(lead);
     return savedLead;
    }

   public Page<Lead> getLeads(int page, int size) {
        // Only fetch leads belonging to the logged-in user's tenant
        String tenantId = TenantContext.getTenantId();
       return leadRepository.findAllByTenantId(
        UUID.fromString(tenantId), 
        PageRequest.of(page, size)
    );
    }
}