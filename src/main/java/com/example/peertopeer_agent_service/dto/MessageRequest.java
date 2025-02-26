package com.example.peertopeer_agent_service.dto;

public class MessageRequest {
    private String companyId;
    private String message;

    // Getters
    public String getCompanyId() {
        return companyId;
    }

    public String getMessage() {
        return message;
    }

    // Setters
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}