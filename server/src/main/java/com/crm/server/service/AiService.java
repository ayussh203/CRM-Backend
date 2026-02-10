package com.crm.server.service;

import com.crm.server.dto.AiResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AiService {

    @Value("${crm.ai.api-key}")
    private String apiKey;

    @Value("${crm.ai.model-url}")
    private String modelUrl;

    private final RestClient restClient = RestClient.create();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AiResult analyzeLead(String company, String source) {
        String prompt = String.format(
            "Analyze '%s' (Source: %s). Return strictly JSON: " +
            "{\"summary\": \"1-sentence description\", \"score\": <0-100 integer>}. " +
            "No markdown.", 
            company, source
        );

        String requestBody = """
            { "contents": [{ "parts": [{"text": "%s"}] }] }
            """.formatted(prompt.replace("\"", "\\\"")); // Escape quotes

        try {
            String response = restClient.post()
                    .uri(modelUrl + "?key=" + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);

            JsonNode root = objectMapper.readTree(response);
            String content = root.path("candidates").get(0)
                                 .path("content").path("parts").get(0)
                                 .path("text").asText();
            
            content = content.replace("```json", "").replace("```", "").trim();
            JsonNode json = objectMapper.readTree(content);

            return new AiResult(
                json.has("summary") ? json.get("summary").asText() : "Analysis failed",
                json.has("score") ? json.get("score").asInt() : 50
            );

        } catch (Exception e) {
            e.printStackTrace();
            return new AiResult("AI Unavailable", 0);
        }
    }
}