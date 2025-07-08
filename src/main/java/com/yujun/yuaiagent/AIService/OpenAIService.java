package com.yujun.yuaiagent.AIService;

import com.yujun.yuaiagent.demo.invoke.OpenAISdkInvoke;
import com.yujun.yuaiagent.demo.invoke.OpenAIHttpInvoke;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpenAIService {

    private final OpenAISdkInvoke openAISdkInvoke;
    private final OpenAIHttpInvoke openAiHttpInvoke;

    @Autowired
    public OpenAIService(OpenAISdkInvoke openAISdkInvoke, OpenAIHttpInvoke openAiHttpInvoke) {
        this.openAISdkInvoke = openAISdkInvoke;
        this.openAiHttpInvoke = openAiHttpInvoke;
    }

    /**
     * Demonstrate the OpenAI-compatible API for Volcengine using SDK
     * @param prompt The user prompt
     * @return The AI response
     */
    public String demonstrateOpenAI(String prompt) {
        System.out.println("Sending prompt to Volcengine via OpenAI SDK: " + prompt);
        String response = openAISdkInvoke.createChatCompletion(prompt);
        System.out.println("Received response from Volcengine via OpenAI SDK: " + response);
        return response;
    }

    /**
     * Demonstrate the OpenAI-compatible API for Volcengine using HTTP
     * @param prompt The user prompt
     * @return The AI response
     */
    public String demonstrateOpenAIHttp(String prompt) {
        System.out.println("Sending prompt to Volcengine via OpenAI-compatible HTTP API: " + prompt);
        String response = openAiHttpInvoke.createChatCompletion(prompt);
        System.out.println("Received response from Volcengine via OpenAI-compatible HTTP API: " + response);
        return response;
    }
}
