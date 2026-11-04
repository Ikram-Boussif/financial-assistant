package com.fintech.financial_assistant.config;

import com.google.genai.Client;
import com.google.genai.types.HttpOptions;
import com.google.genai.types.HttpRetryOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.time.Duration;

/**
 * Custom Google GenAI client.
 * Spring AI does not expose a timeout property for Gemini, so we provide our own
 * {@link Client} bean, which the auto-configuration uses instead of its default one.
 */
@Configuration
public class GeminiClientConfig {

    @Bean
    public Client googleGenAiClient(
            @Value("${spring.ai.google.genai.api-key:}") String apiKey,
            @Value("${app.ai.timeout:20s}") Duration timeout) {

        // Fail fast at startup if the key is missing,
        // instead of failing later on the first call to /api/financial/ask.
        if (!StringUtils.hasText(apiKey)) {
            throw new IllegalStateException(
                    "Missing Gemini API key: set GEMINI_API_KEY in the .env file "
                            + "at the project root (or as an environment variable).");
        }

        HttpOptions httpOptions = HttpOptions.builder()
                // Max duration of ONE HTTP call to Gemini (in milliseconds).
                .timeout((int) timeout.toMillis())
                // Single attempt in the Google SDK: retries are handled by Spring AI
                // (spring.ai.retry.*), so the two retry mechanisms don't stack up.
                .retryOptions(HttpRetryOptions.builder().attempts(1).build())
                .build();

        return Client.builder()
                .apiKey(apiKey)
                .httpOptions(httpOptions)
                .build();
    }
}
