package com.maveric.csp.exceptions;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
        @ExceptionHandler(ConstraintViolationException.class)
        public void constraintViolationException(HttpServletResponse response) throws  IOException {
            response.sendError(HttpStatus.BAD_REQUEST.value());
        }
}
