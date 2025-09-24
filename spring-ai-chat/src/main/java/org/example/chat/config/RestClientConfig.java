package org.example.chat.config;

import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig implements RestClientCustomizer {
    @Override
    public void customize(RestClient.Builder restClientBuilder) {
        restClientBuilder.requestInterceptor(((request, body, execution) -> {
//            String str = new String(body);
//            str = str.substring(0, str.length() - 1) + ",\"enable_thinking\":false}";
//            return execution.execute(request, str.getBytes());
            return execution.execute(request, body);
        }));
    }
}
