package com.crm.server.event;

import java.util.UUID;

public class LeadCreatedEvent {
    private UUID leadId;
    private UUID tenantId;
    private String email;

    public LeadCreatedEvent() {}

    public LeadCreatedEvent(UUID leadId, UUID tenantId, String email) {
        this.leadId = leadId;
        this.tenantId = tenantId;
        this.email = email;
    }

  
    public UUID getLeadId() { return leadId; }
    public void setLeadId(UUID leadId) { this.leadId = leadId; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}