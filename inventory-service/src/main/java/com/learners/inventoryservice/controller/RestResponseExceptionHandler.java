package com.learners.inventoryservice.controller;

import com.learners.inventoryservice.exception.PizzaNotFoundException;
import com.learners.inventoryservice.model.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class RestResponseExceptionHandler {

    @ExceptionHandler(PizzaNotFoundException.class)
    ResponseEntity<ErrorDto> handleException(PizzaNotFoundException e) {
        return new ResponseEntity<>(ErrorDto.of(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorDto> handleException(BindException e) {
        String message = e.getAllErrors().stream()
                .map(error -> error.getDefaultMessage() + "::" + error.getArguments().toString())
                .collect(Collectors.toList()).toString();
        return new ResponseEntity<>(ErrorDto.of(message), HttpStatus.BAD_REQUEST);
    }
}
