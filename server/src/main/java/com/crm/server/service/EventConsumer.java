package com.crm.server.service;

import com.crm.server.event.LeadCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EventConsumer {

    @KafkaListener(topics = "crm.leads", groupId = "crm-group")
    public void handleLeadCreated(LeadCreatedEvent event) {
        // This runs in a separate thread!
        System.out.println(" CONSUMER RECEIVED: New Lead ID " + event.getLeadId() + " for Tenant " + event.getTenantId());
        
//todo ai 
    }
}