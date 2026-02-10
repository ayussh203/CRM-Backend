package com.crm.server.config;

import com.crm.server.entity.Tenant;
import com.crm.server.entity.User;
import com.crm.server.entity.Workflow;
import com.crm.server.repository.TenantRepository;
import com.crm.server.repository.UserRepository;
import com.crm.server.repository.WorkflowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeding implements CommandLineRunner {

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final WorkflowRepository workflowRepository;

    @Override
    public void run(String... args) throws Exception {
        
        Tenant tenant = null;

        // 1. Seed Tenant & User if missing
        if (tenantRepository.count() == 0) {
            System.out.println(" Seeding Tenant & Admin...");
            tenant = Tenant.builder().name("Demo Corp").plan("ENTERPRISE").isActive(true).build();
            tenant = tenantRepository.save(tenant);

            User admin = User.builder()
                    .name("Admin User")
                    .email("admin@demo.com")
                    .passwordHash("secret")
                    .role(User.Role.ADMIN)
                    .tenant(tenant)
                    .build();
            userRepository.save(admin);
        } else {
            // Load existing tenant to use for the workflow
            tenant = tenantRepository.findAll().get(0);
        }

        // 2. Seed Workflow Rules (Check INDEPENDENTLY)
        if (workflowRepository.count() == 0 && tenant != null) {
            System.out.println(" Seeding Workflow Rules");
            Workflow rule = new Workflow(
                "Auto-Qualify Twitter Leads",
                "LEAD_CREATED",
                "{\"field\": \"source\", \"operator\": \"EQUALS\", \"value\": \"Twitter\"}",
                "{\"type\": \"UPDATE_STATUS\", \"value\": \"QUALIFIED\"}",
                tenant
            );
            workflowRepository.save(rule);
            System.out.println(" WORKFLOW SEEDING COMPLETE");
        }
    }
}