package com.crm.server.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "leads")
public class Lead extends BaseEntity {

    private String firstName;
    private String lastName;
    private String email;
    private String company;
    private String source; // e.g., "LinkedIn", "Website"

    @Enumerated(EnumType.STRING)
    private LeadStatus status = LeadStatus.NEW;

    // --- AI Enrichment Fields (For Sprint 5) ---
    @Column(columnDefinition = "TEXT")
    private String aiSummary; // "This company does Fintech..."
    
    private int aiScore; // 0-100 probability to buy

    // --- Multi-Tenancy & Ownership ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner; // The Sales Rep assigned

    public enum LeadStatus {
        NEW, CONTACTED, QUALIFIED, LOST, WON
    }

    // --- Constructors & Builder (Manual to avoid Lombok issues) ---
    public Lead() {}

    public Lead(String firstName, String lastName, String email, String company, String source, Tenant tenant, User owner) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.company = company;
        this.source = source;
        this.tenant = tenant;
        this.owner = owner;
    }
    
    // Getters and Setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public LeadStatus getStatus() { return status; }
    public void setStatus(LeadStatus status) { this.status = status; }
    public String getAiSummary() { return aiSummary; }
    public void setAiSummary(String aiSummary) { this.aiSummary = aiSummary; }
    public int getAiScore() { return aiScore; }
    public void setAiScore(int aiScore) { this.aiScore = aiScore; }
    public Tenant getTenant() { return tenant; }
    public void setTenant(Tenant tenant) { this.tenant = tenant; }
    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }
}