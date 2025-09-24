package org.example.mcpserver.config;

import org.springframework.ai.tool.annotation.Tool;

public class LocationTools {

    @Tool(description = "Get the user's location")
    String getLocation() {
        return "Beijing";
    }
}
