package com.yujun.yuaiagent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class LoveAppTest {

    @Resource
    private LoveApp loveApp;

    @Test
    void testDoChat() {
        String chatId = UUID.randomUUID().toString();

        String messaage = "Hi, I'm Yujun! 我现在处于恋爱中。";
        String answer = loveApp.doChat(messaage, chatId);
        Assertions.assertNotNull(answer);

        messaage = "给我一些个人恋爱建议";
        answer = loveApp.doChat(messaage, chatId);
        Assertions.assertNotNull(answer);

        messaage = "我叫什么名字来着，刚和你说过，回忆一下，以及我现在是单身还是恋爱中啊";
        answer = loveApp.doChat(messaage, chatId);
        Assertions.assertNotNull(answer);

    }


    @Test
    void testDoChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        String message = "你好，我是Yujun，一名程序员，我现在人在中国，但女朋友在外国，我们有12个小时的时差，因为一些原因，我俩要半年以上都见不到面，我该怎么做。";
        LoveApp.LoveReport loveReport = loveApp.doChatWithReport(message, chatId);

        Assertions.assertNotNull(loveReport);
    }
}