package org.example.chat.controller;

import org.example.chat.config.tool.DateTimeTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;

@RestController
public class ChatController {

    private final ChatClient chatClient;

    @Autowired
    private SyncMcpToolCallbackProvider toolCallbackProvider;

    @Autowired
    private QuestionAnswerAdvisor questionAnswerAdvisor;

    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("test/hello")
    public String testHello() {
        return chatClient.prompt("你好")
                .call()
                .content();
    }

    @GetMapping("test/sse")
    public SseEmitter testSse() {
        SseEmitter emitter = new SseEmitter();
        Flux<String> content = chatClient.prompt("你好")
                .stream()
                .content();
        content.subscribe(v -> {
            try {
                emitter.send(v);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, emitter::completeWithError, emitter::complete);
        return emitter;
    }

    @GetMapping("test/tools")
    public String testTools() {
        return chatClient.prompt("设置一个十分钟后的闹钟")
                .tools(new DateTimeTools())
                .call()
                .content();
    }

    @GetMapping("test/mcp")
    public String testMcp() {
        return chatClient.prompt("当前时间")
                .toolCallbacks(toolCallbackProvider)
                .call()
                .content();
    }

    @GetMapping("test/rag")
    public String testRag() {
        return chatClient.prompt("每天的工作时间")
                .advisors(questionAnswerAdvisor)
                .call()
                .content();
    }
}
