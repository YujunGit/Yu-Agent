package com.yujun.yuaiagent.demo.invoke;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatCompletion;
import com.openai.models.ChatCompletionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OpenAISdkInvoke {

    @Value("${openai.api.key:}")
    private String apiKey;

    @Value("${openai.model.id:skylark-chat}")
    private String modelId;

    @Value("${openai.base.url:https://ark.cn-beijing.volces.com/api/v3/}")
    private String baseUrl;

    /**
     * Create a chat completion using OpenAI SDK
     * @param prompt The user prompt
     * @return The AI response
     */
    public String createChatCompletion(String prompt) {
        // If API key is not set in properties, try to get from environment
        if (apiKey == null || apiKey.isEmpty()) {
            apiKey = System.getenv("ARK_API_KEY");
        }

        try {
            // Configure the OpenAI client with custom base URL for Volcengine
            OpenAIClient client = OpenAIOkHttpClient.builder()
                    .apiKey(apiKey)
                    .baseUrl(baseUrl)
                    .build();

            // Build the chat completion parameters
            ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                    .addUserMessage(prompt)
                    .model(modelId)
                    .build();

            // Create the chat completion
            ChatCompletion chatCompletion = client.chat().completions().create(params);

            // Extract and return the response content
            if (chatCompletion != null && chatCompletion.choices() != null && !chatCompletion.choices().isEmpty()) {
                return chatCompletion.choices().get(0).message().content().orElse("No content in response");
            }

            return "No response content";
        } catch (Exception e) {
            System.err.println("Error calling OpenAI SDK: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }
}
