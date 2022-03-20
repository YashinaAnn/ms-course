package com.learners.pizzaservice.controller;

import com.learners.pizzaservice.exception.InventoryServiceException;
import com.learners.pizzaservice.exception.PizzaNotFoundException;
import com.learners.pizzaservice.exception.PizzaServiceException;
import com.learners.pizzaservice.model.ErrorDto;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.boot.context.properties.bind.validation.BindValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleException(MethodArgumentNotValidException e) {
        List<String> body = e.getBindingResult().getAllErrors().stream()
                .map(err -> err.getObjectName() + " :: " + err.getDefaultMessage())
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(PizzaNotFoundException.class)
    public ResponseEntity<ErrorDto> handleException(PizzaNotFoundException e) {
        return new ResponseEntity<>(ErrorDto.of(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PizzaServiceException.class)
    public ResponseEntity<ErrorDto> handleException(PizzaServiceException e) {
        return ResponseEntity.badRequest().body(ErrorDto.of(e.getMessage()));
    }
}
