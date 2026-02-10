package com.crm.server.service;

import com.crm.server.dto.CreateTenantRequest;
import com.crm.server.entity.Tenant;
import com.crm.server.entity.User;
import com.crm.server.repository.TenantRepository;
import com.crm.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;

    @Transactional //  Rollback if user creation fails
    public Tenant createTenant(CreateTenantRequest request) {
        // 1. Create Tenant
        Tenant tenant = new Tenant();
        tenant.setName(request.name());
        tenant.setPlan(request.plan());
        tenant.setActive(true);
        Tenant savedTenant = tenantRepository.save(tenant);

    
        User admin = User.builder()
                .name("Admin")
                .email(request.adminEmail())
                .passwordHash(request.adminPassword()) 
                .role(User.Role.ADMIN)
                .tenant(savedTenant)
                .build();
        userRepository.save(admin);

        return savedTenant;
    }
}