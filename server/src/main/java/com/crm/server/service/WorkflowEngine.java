package com.crm.server.service;

import com.crm.server.entity.Lead;
import com.crm.server.entity.Workflow;
import com.crm.server.repository.LeadRepository;
import com.crm.server.repository.WorkflowRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkflowEngine {

    private final WorkflowRepository workflowRepository;
    private final LeadRepository leadRepository;
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON Parser

    public void evaluate(Lead lead, String eventType) {
        System.out.println(" Evaluating Rules for: " + eventType);

        // 1. Fetch Rules for this Tenant & Event
        List<Workflow> workflows = workflowRepository.findAllByTenantIdAndTriggerEventAndIsActiveTrue(
                lead.getTenant().getId(), 
                eventType
        );

        for (Workflow workflow : workflows) {
            try {
                if (matchesCondition(lead, workflow.getConditions())) {
                    executeAction(lead, workflow.getActions());
                }
            } catch (Exception e) {
                System.err.println("Rule Execution Failed: " + workflow.getName());
                e.printStackTrace();
            }
        }
    }

    private boolean matchesCondition(Lead lead, String conditionJson) throws Exception {
        JsonNode node = objectMapper.readTree(conditionJson);
        String field = node.get("field").asText();       // e.g., "source"
        String operator = node.get("operator").asText(); // e.g., "EQUALS"
        String value = node.get("value").asText();       // e.g., "Twitter"

        // Dynamic Reflection Logic 
        if ("source".equalsIgnoreCase(field) && "EQUALS".equalsIgnoreCase(operator)) {
            return value.equalsIgnoreCase(lead.getSource());
        }
        // Add more fields here later (company, email, etc.) todo
        return false;
    }

    private void executeAction(Lead lead, String actionJson) throws Exception {
        JsonNode node = objectMapper.readTree(actionJson);
        String type = node.get("type").asText();         // e.g., "UPDATE_STATUS"
        String value = node.get("value").asText();       // e.g., "QUALIFIED"

        if ("UPDATE_STATUS".equalsIgnoreCase(type)) {
            System.out.println(" RULE MATCH! Updating Status to: " + value);
            lead.setStatus(Lead.LeadStatus.valueOf(value));
            leadRepository.save(lead);
        }
    }
}