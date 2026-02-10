package com.crm.server.dto;

public record CreateTenantRequest(
    String name,          // e.g., "Google"
    String plan,          // e.g., "ENTERPRISE"
    String adminEmail,    // e.g., "larry@google.com"
    String adminPassword  // e.g., "secret123"
) {}