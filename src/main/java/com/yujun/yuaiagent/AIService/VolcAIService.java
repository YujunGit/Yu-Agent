package com.yujun.yuaiagent.AIService;

import com.yujun.yuaiagent.demo.invoke.VolcSdkInvoke;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VolcAIService {

    private final VolcSdkInvoke volcSdkInvoke;

    @Autowired
    public VolcAIService(VolcSdkInvoke volcSdkInvoke) {
        this.volcSdkInvoke = volcSdkInvoke;
    }

    /**
     * Demonstrate the Volc AI service functionality
     * @param prompt The user prompt
     * @return The AI response
     */
    public String demonstrateVolcAI(String prompt) {
        System.out.println("Sending prompt to Volc AI: " + prompt);
        String response = volcSdkInvoke.createChatCompletion(prompt);
        System.out.println("Received response from Volc AI: " + response);
        return response;
    }
}
