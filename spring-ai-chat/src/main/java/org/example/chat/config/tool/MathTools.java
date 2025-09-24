package org.example.chat.config.tool;

import org.springframework.ai.tool.annotation.Tool;

public class MathTools {

    @Tool(description = "Calculate the subtraction of two numbers")
    int add(int a, int b) {
        return a + b;
    }

    @Tool(description = "Calculate the subtraction of two numbers")
    int sub(int a, int b) {
        return a - b;
    }

    @Tool(description = "Calculate the multiplication of two numbers")
    int mul(int a, int b) {
        return a * b;
    }

    @Tool(description = "Calculate the division of two numbers")
    int div(int a, int b) {
        return a / b;
    }
}
