package com.crm.server.service;

import com.crm.server.event.LeadCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafkaTemplate.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "crm.leads";

    public void publishLeadCreated(LeadCreatedEvent event) {
        System.out.println(" Publishing Event: Lead Created " + event.getEmail());
        kafkaTemplate.send(TOPIC, event);
    }
}