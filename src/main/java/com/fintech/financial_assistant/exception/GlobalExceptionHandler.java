package com.fintech.financial_assistant.exception;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

/**
 * Turns AI errors into clear HTTP responses using the standard
 * "Problem Details" format (RFC 9457): {"type","title","status","detail","instance"}.
 * Texts come from messages*.properties, in the language of the request's Accept-Language header.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(AiUnavailableException.class)
    public ResponseEntity<ProblemDetail> handleAiUnavailable(AiUnavailableException ex, Locale locale) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .header(HttpHeaders.RETRY_AFTER, "30") // the client may retry in 30 s
                .body(problem(HttpStatus.SERVICE_UNAVAILABLE, "error.ai.unavailable", locale));
    }

    @ExceptionHandler(AiTimeoutException.class)
    public ResponseEntity<ProblemDetail> handleAiTimeout(AiTimeoutException ex, Locale locale) {
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
                .body(problem(HttpStatus.GATEWAY_TIMEOUT, "error.ai.timeout", locale));
    }

    /** Builds a ProblemDetail from the "<key>.title" and "<key>.detail" messages. */
    private ProblemDetail problem(HttpStatus status, String key, Locale locale) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                status, messageSource.getMessage(key + ".detail", null, locale));
        problem.setTitle(messageSource.getMessage(key + ".title", null, locale));
        return problem;
    }
}
