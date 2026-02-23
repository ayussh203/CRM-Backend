package com.crm.server.controller;

import com.crm.server.entity.AuditLog;
import com.crm.server.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    @Autowired
    private final AuditLogRepository auditLogRepository;
    @GetMapping("/{leadId}")
    public ResponseEntity<List<AuditLog>> getLogs(@PathVariable UUID leadId) {
        return ResponseEntity.ok(auditLogRepository.findAllByLeadIdOrderByTimestampDesc(leadId));
    }
}