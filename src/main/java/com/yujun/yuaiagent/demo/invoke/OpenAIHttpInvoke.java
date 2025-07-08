package com.yujun.yuaiagent.demo.invoke;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OpenAIHttpInvoke {

    @Value("${openai.api.key:}")
    private String apiKey;

    @Value("${openai.model.id:skylark-chat}")
    private String modelId;

    @Value("${openai.base.url:https://ark.cn-beijing.volces.com/api/v3/}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Create a chat completion using OpenAI-compatible API via HTTP
     * @param prompt The user prompt
     * @return The AI response
     */
    public String createChatCompletion(String prompt) {
        // If API key is not set in properties, try to get from environment
        if (apiKey == null || apiKey.isEmpty()) {
            apiKey = System.getenv("ARK_API_KEY");
        }

        // Set up headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        // Create request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", modelId);

        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);
        messages.add(userMessage);

        requestBody.put("messages", messages);

        // Create HTTP entity
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // Make API call
        String url = baseUrl + "chat/completions";
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

            // Extract response content
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> choice = choices.get(0);
                    Map<String, String> message = (Map<String, String>) choice.get("message");
                    return message.get("content");
                }
            }
            return "No response content";
        } catch (Exception e) {
            System.err.println("Error calling OpenAI-compatible API: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }
}