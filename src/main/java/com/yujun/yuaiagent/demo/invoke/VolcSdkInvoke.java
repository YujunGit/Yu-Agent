package com.yujun.yuaiagent.demo.invoke;

import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class VolcSdkInvoke {

    @Value("${volc.api.key:}")
    private String apiKey;

    @Value("${volc.model.id:skylark-chat}")
    private String modelId;

    /**
     * Create a chat completion using Volc SDK
     * @param prompt The user prompt
     * @return The AI response
     */
    public String createChatCompletion(String prompt) {
        // If API key is not set in properties, try to get from environment
        if (apiKey == null || apiKey.isEmpty()) {
            apiKey = System.getenv("ARK_API_KEY");
        }

        // Build ArkService
        ArkService service = ArkService.builder()
                .apiKey(apiKey)
                .timeout(Duration.ofSeconds(1800)) // Set backend processing timeout to 1800 seconds
                .connectTimeout(Duration.ofSeconds(20)) // Set connection timeout to 20 seconds
                .retryTimes(2) // Set retry times to 2
                .build();

        try {
            // Create messages list
            final List<ChatMessage> messages = new ArrayList<>();
            final ChatMessage userMessage = ChatMessage.builder()
                    .role(ChatMessageRole.USER)
                    .content(prompt)
                    .build();
            messages.add(userMessage);

            // Build chat completion request
            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                    .model(modelId) // Use model ID from properties or default
                    .messages(messages)
                    .build();

            // Get response
            StringBuilder responseBuilder = new StringBuilder();
            service.createChatCompletion(chatCompletionRequest).getChoices()
                    .forEach(choice -> responseBuilder.append(choice.getMessage().getContent()));

            return responseBuilder.toString();
        } finally {
            // Shutdown service executor
            service.shutdownExecutor();
        }
    }
}
