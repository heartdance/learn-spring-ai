package org.example.mcpserver.config;

import java.util.function.Function;

public class WeatherService implements Function<WeatherService.WeatherRequest, WeatherService.WeatherResponse> {

    public WeatherResponse apply(WeatherRequest request) {
        return new WeatherResponse(30.0, Unit.C);
    }

    public enum Unit { C, F }
    public record WeatherRequest(String location, Unit unit) {}
    public record WeatherResponse(double temp, Unit unit) {}
}
