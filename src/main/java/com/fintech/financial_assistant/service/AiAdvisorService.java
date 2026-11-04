package com.fintech.financial_assistant.service;

import com.fintech.financial_assistant.exception.AiTimeoutException;
import com.fintech.financial_assistant.exception.AiUnavailableException;
import com.google.genai.errors.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.io.InterruptedIOException;

@Service
public class AiAdvisorService {

    private static final Logger log = LoggerFactory.getLogger(AiAdvisorService.class);

    private static final String SYSTEM_PROMPT = """
            Tu es un assistant financier personnel. Tu donnes des conseils clairs, concrets et prudents
            sur le budget, l'épargne, l'endettement et l'investissement.
            Tu rappelles que tes réponses ne remplacent pas l'avis d'un conseiller financier agréé.
            Réponds dans la langue de l'utilisateur.
            """;

    private final ChatClient chatClient;

    public AiAdvisorService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultSystem(SYSTEM_PROMPT)
                .build();
    }

    public String ask(String question) {
        try {
            return this.chatClient.prompt()
                    .user(question)
                    .call()
                    .content();
        } catch (RuntimeException ex) {
            throw translate(ex);
        }
    }

    /**
     * Translates technical errors (Google SDK, Spring AI) into business exceptions.
     */
    private RuntimeException translate(RuntimeException ex) {
        for (Throwable cause = ex; cause != null; cause = cause.getCause()) {
            if (cause instanceof InterruptedIOException) { // includes SocketTimeoutException
                log.warn("Gemini call timed out", ex);
                return new AiTimeoutException("Gemini call timed out", ex);
            }
            if (cause instanceof ApiException api && (api.code() >= 500 || api.code() == 429)) {
                log.warn("Gemini unavailable (HTTP {}): {}", api.code(), api.message());
                return new AiUnavailableException("Gemini unavailable (HTTP " + api.code() + ")", ex);
            }
        }
        return ex; // any other error (bug, bad config...): let it propagate as a 500
    }
}
