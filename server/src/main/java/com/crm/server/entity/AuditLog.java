package com.crm.server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
@Getter @Setter @NoArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID leadId;    // Which Lead was changed?
    private UUID userId;    // Who did it?
    private String action;  // "UPDATE", "DELETE", "Create"
    
    @Column(columnDefinition = "TEXT")
    private String details; // "Changed status from NEW to QUALIFIED"

    private LocalDateTime timestamp = LocalDateTime.now();

    public AuditLog(UUID leadId, UUID userId, String action, String details) {
        this.leadId = leadId;
        this.userId = userId;
        this.action = action;
        this.details = details;
    }
}