package com.fintech.financial_assistant.exception;

/** The AI service (Gemini) did not respond within the allowed time. */
public class AiTimeoutException extends RuntimeException {
    public AiTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
