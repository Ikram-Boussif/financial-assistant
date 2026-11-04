package com.fintech.financial_assistant.service;

import com.fintech.financial_assistant.exception.AiTimeoutException;
import com.fintech.financial_assistant.exception.AiUnavailableException;
import com.google.genai.errors.ClientException;
import com.google.genai.errors.ServerException;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;

import java.net.SocketTimeoutException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AiAdvisorServiceTest {

    /** Creates a service whose Gemini call throws the given exception. */
    private AiAdvisorService serviceThrowing(RuntimeException error) {
        ChatClient chatClient = mock(ChatClient.class, RETURNS_DEEP_STUBS);
        when(chatClient.prompt().user(anyString()).call().content()).thenThrow(error);

        ChatClient.Builder builder = mock(ChatClient.Builder.class);
        when(builder.defaultSystem(anyString())).thenReturn(builder);
        when(builder.build()).thenReturn(chatClient);
        return new AiAdvisorService(builder);
    }

    @Test
    void gemini503_becomesAiUnavailable() {
        // Same wrapping as Spring AI: RuntimeException("Failed to generate content") -> ServerException
        var error = new RuntimeException("Failed to generate content",
                new ServerException(503, "UNAVAILABLE", "This model is currently experiencing high demand."));

        assertThatThrownBy(() -> serviceThrowing(error).ask("question"))
                .isInstanceOf(AiUnavailableException.class);
    }

    @Test
    void gemini429_becomesAiUnavailable() {
        var error = new RuntimeException(new ClientException(429, "RESOURCE_EXHAUSTED", "Quota exceeded"));

        assertThatThrownBy(() -> serviceThrowing(error).ask("question"))
                .isInstanceOf(AiUnavailableException.class);
    }

    @Test
    void timeout_becomesAiTimeout() {
        var error = new RuntimeException(new SocketTimeoutException("timeout"));

        assertThatThrownBy(() -> serviceThrowing(error).ask("question"))
                .isInstanceOf(AiTimeoutException.class);
    }

    @Test
    void otherClientError_isRethrownAsIs() {
        // e.g. unknown model (404): a configuration bug, not an outage -> 500
        var error = new RuntimeException(new ClientException(404, "NOT_FOUND", "model not found"));

        assertThatThrownBy(() -> serviceThrowing(error).ask("question"))
                .isSameAs(error);
    }
}
