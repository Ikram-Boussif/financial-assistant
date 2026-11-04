package com.fintech.financial_assistant.exception;

/** The AI service (Gemini) is unavailable or overloaded. */
public class AiUnavailableException extends RuntimeException {
    public AiUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
