package com.yujun.yuaiagent.demo.invoke;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONArray;

/**
 * HttpAiInvoke - 使用阿里云百炼 DashScope 接口的文本生成调用类
 * 使用 Hutool 构建 HTTP 请求，支持多轮对话、结构化输出
 */
public class HttpAiInvoke {

    // DashScope 接口地址
    private static final String API_URL = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";

    // 替换为你的 DashScope API Key（建议从配置文件或环境变量中读取）
    private static final String API_KEY = TestApiKey.API_KEY;

    /**
     * 调用 DashScope 的 qwen-plus 模型进行对话生成
     */
    public static void main(String[] args) {
        // 构造 system 提示：设定 AI 的行为风格
        JSONObject systemMessage = new JSONObject()
                .set("role", "system")
                .set("content", "You are a helpful assistant.");

        // 构造 user 提问
        JSONObject userMessage = new JSONObject()
                .set("role", "user")
                .set("content", "你是谁？");

        // 合并为消息数组（ChatML 格式）
        JSONArray messages = new JSONArray();
        messages.add(systemMessage);
        messages.add(userMessage);

        // input 字段封装对话内容
        JSONObject input = new JSONObject()
                .set("messages", messages);

        // 设置返回格式为 message 类型（支持 text 或 message）
        JSONObject parameters = new JSONObject()
                .set("result_format", "message");

        // 构造请求体
        JSONObject requestBody = new JSONObject()
                .set("model", "qwen-plus") // 指定模型
                .set("input", input)
                .set("parameters", parameters);

        try {
            // 发送 POST 请求，包含必要的头部和 JSON 请求体
            HttpResponse response = HttpRequest.post(API_URL)
                    .header("Authorization", "Bearer " + API_KEY) // 设置 Bearer Token
                    .header("Content-Type", ContentType.JSON.toString()) // 设置请求类型为 JSON
                    .body(requestBody.toString()) // 设置请求体
                    .execute(); // 执行请求

            // 输出响应状态码和返回结果
            System.out.println("Status: " + response.getStatus());
            System.out.println("Response Body: " + response.body());

        } catch (Exception e) {
            // 捕获并打印错误（生产环境建议写入日志）
            System.err.println("Error during DashScope API call:");
            e.printStackTrace();
        }
    }
}