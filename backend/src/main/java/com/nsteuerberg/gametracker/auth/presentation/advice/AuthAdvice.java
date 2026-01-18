package com.nsteuerberg.gametracker.auth.presentation.advice;

import com.nsteuerberg.gametracker.auth.service.exceptions.InvalidTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthAdvice {
    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String invalidTokenException(InvalidTokenException ex) {
        return ex.getMessage();
    }
}
