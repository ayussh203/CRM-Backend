package com.crm.server.dto;

public record WorkflowDTO(
    String name,        // "Auto-Qualify Google"
    String triggerEvent,// "LEAD_CREATED"
    String conditions,  // JSON String
    String actions      // JSON String
) {}