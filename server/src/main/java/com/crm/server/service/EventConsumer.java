package com.crm.server.service;

import com.crm.server.config.TenantContext;
import com.crm.server.entity.Lead;
import com.crm.server.event.LeadCreatedEvent;
import com.crm.server.repository.LeadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventConsumer {

    private final LeadRepository leadRepository;
    private final AiService aiService;

    @KafkaListener(topics = "crm.leads", groupId = "crm-group")
    public void handleLeadCreated(LeadCreatedEvent event) {
        System.out.println(" AI Worker started for Lead: " + event.getEmail());

        TenantContext.setTenantId(event.getTenantId().toString());

        try {
            // 2. Fetch the Lead
            Lead lead = leadRepository.findById(event.getLeadId())
                    .orElseThrow(() -> new RuntimeException("Lead not found"));

            // 3. Process AI Logic (Simulated)
            String summary = aiService.generateSummary(lead.getCompany(), lead.getSource());
            int score = aiService.calculateScore(lead.getSource());

            // 4. Update the Database
            lead.setAiSummary(summary);
            lead.setAiScore(score);
            lead.setStatus(Lead.LeadStatus.QUALIFIED); // Auto-qualify if score is high

            leadRepository.save(lead);

            System.out.println(" AI Processing Complete. Score: " + score);

        } catch (Exception e) {
            System.err.println(" AI Processing Failed: " + e.getMessage());
        } finally {
            // Always clear context
            TenantContext.clear();
        }
    }
}