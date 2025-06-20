package com.yujun.yuaiagent.demo.invoke;

import jakarta.annotation.Resource;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Spring AI 框架调用AI LLM(Ali)
 */
@Component
public class SpringAIAiInvoke implements CommandLineRunner {

    @Resource
    private ChatModel dashscopeChatModel;

    @Override
    public void run(String... args) throws Exception {
        AssistantMessage assistantMessage = dashscopeChatModel.call(new Prompt("Hi, I'm Yujun, I'm trying Spring AI for the first time."))
                .getResult()
                .getOutput();
        System.out.println(assistantMessage.getText());
    }
}
