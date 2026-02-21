package com.nsteuerberg.gametracker.auth.presentation.advice;

import com.nsteuerberg.gametracker.auth.service.exceptions.InvalidTokenException;
import com.nsteuerberg.gametracker.library.service.exceptions.UnauthorizedAccessException;
import com.nsteuerberg.gametracker.shared.ProblemDetailBuilder;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthAdvice {
    private final static Logger log = LoggerFactory.getLogger(AuthAdvice.class);

    @ExceptionHandler(InvalidTokenException.class)
    public ProblemDetail handleInvalidTokenException(InvalidTokenException ex, HttpServletRequest request) {
        log.warn("Invalid Token: {} en el path {}", ex.getMessage(), request.getRequestURI());
        return ProblemDetailBuilder.build(HttpStatus.UNAUTHORIZED, ex.getMessage(), "INVALID_TOKEN", request.getRequestURI());
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ProblemDetail handleUnauthorizedAccessException(UnauthorizedAccessException ex, HttpServletRequest request) {
        log.warn("Unauthorized access: {} en el path {}", ex.getMessage(), request.getRequestURI());
        return ProblemDetailBuilder.build(HttpStatus.UNAUTHORIZED, ex.getMessage(), "UNAUTHORIZED", request.getRequestURI());
    }
}
