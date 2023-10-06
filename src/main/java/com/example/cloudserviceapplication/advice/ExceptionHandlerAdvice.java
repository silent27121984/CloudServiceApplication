package com.example.cloudserviceapplication.advice;

import com.example.cloudserviceapplication.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.cloudserviceapplication.exceptions.AuthenticationException;
import com.example.cloudserviceapplication.exceptions.BadCredentialsException;

import java.io.IOException;
@Validated
@RestControllerAdvice
public class ExceptionHandlerAdvice {
    private final Logger logger = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {BindException.class, BadCredentialsException.class, IOException.class})
    ErrorResponse handleBindException(Exception e) {
        logger.error(e.getMessage());
        return new ErrorResponse(e.getMessage(), -1);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    ErrorResponse handleAuthenticationException(AuthenticationException e) {
        logger.error(e.getMessage());
        return new ErrorResponse(e.getMessage(), -1);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    ErrorResponse handleRuntimeException(RuntimeException e) {
        logger.error(e.getMessage());
        return new ErrorResponse(e.getMessage(), -1);
    }
}
