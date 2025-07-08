package com.yujun.yuaiagent;

import com.yujun.yuaiagent.AIService.OpenAIService;
import com.yujun.yuaiagent.AIService.VolcAIService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class YuAiAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(YuAiAgentApplication.class, args);
    }

    @Bean
    public CommandLineRunner demonstrateVolcAI(VolcAIService volcAIService) {
        return args -> {
            System.out.println("=== Demonstrating Volc AI Service ===");
            String prompt = "你好，我正在尝试用SDK调用火山引擎的API";
            String response = volcAIService.demonstrateVolcAI(prompt);
            System.out.println("=== Demo completed successfully ===");
        };
    }

    @Bean
    public CommandLineRunner demonstrateOpenAI(OpenAIService openAIService) {
        return args -> {
            // Demonstrate OpenAI SDK implementation
            System.out.println("=== Demonstrating OpenAI SDK for Volcengine ===");
            String sdkPrompt = "你好，我正在尝试用OpenAI的SDK调用火山引擎API";
            String sdkResponse = openAIService.demonstrateOpenAI(sdkPrompt);
            System.out.println("=== OpenAI SDK demo completed successfully ===");

            // Demonstrate OpenAI HTTP implementation
            System.out.println("\n=== Demonstrating OpenAI-compatible HTTP API for Volcengine ===");
            String httpPrompt = "你好，我正在尝试用HTTP方式调用火山引擎的OpenAI兼容API";
            String httpResponse = openAIService.demonstrateOpenAIHttp(httpPrompt);
            System.out.println("=== OpenAI-compatible HTTP API demo completed successfully ===");
        };
    }
}
