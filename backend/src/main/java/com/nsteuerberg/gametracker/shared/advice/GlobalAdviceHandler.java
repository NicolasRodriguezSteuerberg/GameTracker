package com.nsteuerberg.gametracker.shared.advice;

import com.nsteuerberg.gametracker.shared.ProblemDetailBuilder;
import com.nsteuerberg.gametracker.shared.exceptions.EntityConflictException;
import com.nsteuerberg.gametracker.shared.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class GlobalAdviceHandler {
    private final static Logger log = LoggerFactory.getLogger(GlobalAdviceHandler.class);

    @ExceptionHandler(EntityConflictException.class)
    public ProblemDetail handleEntityConflictException(EntityConflictException ex, HttpServletRequest request) {
        log.warn("Conflicto de entidad: {} - Valor: {} en el path: {}", ex.getEntityName(), ex.getFieldValue(), request.getRequestURI());
        return ProblemDetailBuilder.build(HttpStatus.CONFLICT, ex.getMessage(), "Recurso ya existente", request.getRequestURI());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIlleglArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        log.warn("Validación erronea: {} en path {}", ex.getMessage(), request.getRequestURI());
        return ProblemDetailBuilder.build(HttpStatus.BAD_REQUEST, ex.getMessage(), "Bad Request", request.getRequestURI());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        log.warn("Entidad inexistente en el path: {}, msg: {}", request.getRequestURI(), ex.getMessage());
        return ProblemDetailBuilder.build(HttpStatus.NOT_FOUND, ex.getMessage(), "Resource Not Found", request.getRequestURI());
    }
}
