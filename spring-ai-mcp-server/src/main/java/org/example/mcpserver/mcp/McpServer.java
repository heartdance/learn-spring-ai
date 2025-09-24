package org.example.mcpserver.mcp;

import org.example.mcpserver.config.DateTimeTools;
import org.example.mcpserver.config.LocationTools;
import org.example.mcpserver.config.WeatherService;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class McpServer {

    @Bean
    public ToolCallbackProvider functionTools() {
        ToolCallback toolCallback = FunctionToolCallback
                .builder("currentWeather", new WeatherService())
                .description("Get the weather in location")
                .inputType(WeatherService.WeatherRequest.class)
                .build();
        List<ToolCallback> tools = List.of(toolCallback);
        return ToolCallbackProvider.from(tools);
    }

    @Bean
    public ToolCallbackProvider methodTools() {
        return MethodToolCallbackProvider.builder()
                .toolObjects(new DateTimeTools(), new LocationTools())
                .build();
    }
}
