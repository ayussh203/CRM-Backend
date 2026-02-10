package com.crm.server.service;

import org.springframework.stereotype.Service;

@Service
public class AiService {

    // Simulates a call to a Large Language Model (LLM)
    public String generateSummary(String company, String source) {
        // In a real app, this would be an HTTP call to OpenAI/Gemini
        try {
            Thread.sleep(2000); // Simulate 2s delay (Network latency)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "AI Analysis: " + company + " is a high-potential company found via " + source + ". Suggested approach: Email the CTO.";
    }

    public int calculateScore(String source) {
        if ("Referral".equalsIgnoreCase(source)) return 90;
        if ("LinkedIn".equalsIgnoreCase(source)) return 75;
        return 40;
    }
}