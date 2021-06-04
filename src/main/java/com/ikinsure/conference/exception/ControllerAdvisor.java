package com.ikinsure.conference.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @Autowired
    public ControllerAdvisor(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(Exception e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", e.getLocalizedMessage());
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(LocalisedException.class)
    public ResponseEntity<Object> handleLocalised(LocalisedException e, Locale locale) {
        String message = messageSource.getMessage(e.getMessage(), new Object[]{},locale);
        Map<String, String> errors = new HashMap<>();
        errors.put("message", message);
        return new ResponseEntity<>(errors, HttpStatus.BAD_GATEWAY);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_GATEWAY);
    }


}
