package com.crm.server.repository;

import com.crm.server.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    // Fetch logs for a specific lead, newest first
    List<AuditLog> findAllByLeadIdOrderByTimestampDesc(UUID leadId);
}