package com.crm.server.dto;

public class LeadDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String company;
    private String source;

    // Getters & Setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
}