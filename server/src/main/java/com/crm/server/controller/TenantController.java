package com.crm.server.controller;

import com.crm.server.dto.CreateTenantRequest;
import com.crm.server.entity.Tenant;
import com.crm.server.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    // POST /api/tenants - Open endpoint (No Auth required for onboarding)
    @PostMapping
    public ResponseEntity<Tenant> createTenant(@RequestBody CreateTenantRequest request) {
        return ResponseEntity.ok(tenantService.createTenant(request));
    }
}