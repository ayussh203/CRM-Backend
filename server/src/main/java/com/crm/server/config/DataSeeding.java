package com.crm.server.config;

import com.crm.server.entity.Tenant;
import com.crm.server.entity.User;
import com.crm.server.repository.TenantRepository;
import com.crm.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeding implements CommandLineRunner {

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Only seed if the database is empty
        if (tenantRepository.count() == 0) {
            System.out.println("🌱 Starting Data Seeding...");

            // 1. Create Default Tenant
            Tenant tenant = Tenant.builder()
                    .name("Demo Corp")
                    .plan("ENTERPRISE")
                    .isActive(true)
                    .build();
            tenant = tenantRepository.save(tenant);
            
            // 2. Create Admin User linked to that Tenant
            User admin = User.builder()
                    .name("Admin User")
                    .email("admin@demo.com")
                    .passwordHash("secret") // We will encrypt this in Sprint 2
                    .role(User.Role.ADMIN)
                    .tenant(tenant)
                    .build();
            userRepository.save(admin);
            
            System.out.println("✅ SEEDING SUCCESS: Tenant 'Demo Corp' and User 'admin@demo.com' created!");
        } else {
            System.out.println("ℹ️ Data already exists. Skipping seeding.");
        }
    }
}