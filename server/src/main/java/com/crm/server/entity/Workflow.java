package com.crm.server.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "workflows")
public class Workflow extends BaseEntity {

    private String name;           
    private String triggerEvent;   
    private boolean isActive = true;

    @Column(columnDefinition = "TEXT")
    private String conditions;

    // Example: {"actionType": "UPDATE_SCORE", "value": "100"}
    @Column(columnDefinition = "TEXT")
    private String actions;

   
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    public Workflow() {}

    public Workflow(String name, String triggerEvent, String conditions, String actions, Tenant tenant) {
        this.name = name;
        this.triggerEvent = triggerEvent;
        this.conditions = conditions;
        this.actions = actions;
        this.tenant = tenant;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTriggerEvent() { return triggerEvent; }
    public void setTriggerEvent(String triggerEvent) { this.triggerEvent = triggerEvent; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    public String getConditions() { return conditions; }
    public void setConditions(String conditions) { this.conditions = conditions; }
    public String getActions() { return actions; }
    public void setActions(String actions) { this.actions = actions; }
    public Tenant getTenant() { return tenant; }
    public void setTenant(Tenant tenant) { this.tenant = tenant; }
}