package com.crm.server.config;

// This class uses ThreadLocal to store data specific to the current request thread.
// It is the standard way to handle multi-tenancy in Spring Boot.
public class TenantContext {

    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();

    public static void setTenantId(String tenantId) {
        currentTenant.set(tenantId);
    }

    public static String getTenantId() {
        return currentTenant.get();
    }

    public static void clear() {
        currentTenant.remove();
    }
}