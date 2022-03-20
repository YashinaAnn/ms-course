package com.learners.pizzaservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDto {

    private String message;

    public static ErrorDto of(String message) {
        return ErrorDto.builder()
                .message(message)
                .build();
    }
}
