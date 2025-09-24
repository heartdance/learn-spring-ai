package org.example.chat.config.tool;

import org.springframework.ai.tool.annotation.Tool;

public class LocationTools {

    @Tool(description = "Get the user's location")
    String getLocation() {
        return "Beijing";
    }
}
