package com.learners.orderservice.controller;

import com.learners.orderservice.exception.CustomerNotFoundException;
import com.learners.orderservice.exception.OrderNotFoundException;
import com.learners.model.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestResponseExceptionHandler {

    @ExceptionHandler({CustomerNotFoundException.class, OrderNotFoundException.class})
    public ResponseEntity<ErrorDto> handleException(Exception e) {
        return new ResponseEntity<>(ErrorDto.of(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().stream()
                .map(error -> error.getObjectName()
                        + " :: "
                        + Arrays.stream(error.getCodes()).map(String::toString).collect(Collectors.toList())
                        + " :: " +
                        error.getDefaultMessage())
                .collect(Collectors.toList()).toString();
        return new ResponseEntity<>(ErrorDto.of(message), HttpStatus.BAD_REQUEST);
    }
}
