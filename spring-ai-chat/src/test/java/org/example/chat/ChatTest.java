package org.example.chat;

import org.example.chat.config.tool.DateTimeTools;
import org.example.chat.config.tool.MathTools;
import org.example.chat.config.tool.WeatherService;
import org.example.chat.config.tool.WeatherTools;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.DefaultToolCallingManager;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

@SpringBootTest
public class ChatTest {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private ChatModel chatModel;

    @Test
    public void test() {
        String content = chatClient.prompt("你好")
                .advisors(new SimpleLoggerAdvisor())
//                .system("/no_think")
                .call()
                .content();
        System.out.println(content);
    }

    @Test
    public void test1() {
        String content = chatClient.prompt("设置一个十分钟后的闹钟")
                .tools(new DateTimeTools())
                .call()
                .content();
        System.out.println(content);
    }

    @Test
    public void test2() {
        String content = chatClient.prompt("当前时间")
                .tools(new DateTimeTools())
                .call()
                .content();
        System.out.println(content);
    }

    @Test
    public void test3() {
        String content = chatClient.prompt("100乘以123等于多少")
                .tools(new MathTools())
                .call()
                .content();
        System.out.println(content);
    }

    @Test
    public void test4() {
        String content = chatClient.prompt("今天天气怎么样")
                .toolNames(WeatherTools.CURRENT_WEATHER_TOOL)
                .call()
                .content();
        System.out.println(content);
    }

    @Test
    public void test5() {
        ToolCallback toolCallback = FunctionToolCallback
                .builder("currentWeather", new WeatherService())
                .description("Get the weather in location")
                .inputType(WeatherService.WeatherRequest.class)
                .build();
        String content = chatClient.prompt("今天天气怎么样")
                .toolCallbacks(toolCallback)
                .call()
                .content();
        System.out.println(content);
    }

    @Test
    public void test6() {
        ToolCallingManager toolCallingManager = DefaultToolCallingManager.builder().build();
        ChatMemory chatMemory = MessageWindowChatMemory.builder().build();
        String conversationId = UUID.randomUUID().toString();

        ChatOptions chatOptions = ToolCallingChatOptions.builder()
                .toolCallbacks(ToolCallbacks.from(new MathTools()))
                .internalToolExecutionEnabled(false)
                .build();
        Prompt prompt = new Prompt(
                List.of(new SystemMessage("You are a helpful assistant."), new UserMessage("What is 6 * 8?")),
                chatOptions);
        chatMemory.add(conversationId, prompt.getInstructions());

        Prompt promptWithMemory = new Prompt(chatMemory.get(conversationId), chatOptions);
        ChatResponse chatResponse = chatModel.call(promptWithMemory);
        chatMemory.add(conversationId, chatResponse.getResult().getOutput());

        while (chatResponse.hasToolCalls()) {
            ToolExecutionResult toolExecutionResult = toolCallingManager.executeToolCalls(promptWithMemory,
                    chatResponse);
            chatMemory.add(conversationId, toolExecutionResult.conversationHistory()
                    .get(toolExecutionResult.conversationHistory().size() - 1));
            promptWithMemory = new Prompt(chatMemory.get(conversationId), chatOptions);
            chatResponse = chatModel.call(promptWithMemory);
            chatMemory.add(conversationId, chatResponse.getResult().getOutput());
        }

        UserMessage newUserMessage = new UserMessage("What did I ask you earlier?");
        chatMemory.add(conversationId, newUserMessage);

        ChatResponse newResponse = chatModel.call(new Prompt(chatMemory.get(conversationId)));
    }
}
