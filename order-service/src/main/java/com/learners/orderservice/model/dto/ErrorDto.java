package com.learners.orderservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorDto {

    private String message;

    public static ErrorDto of(String message) {
        return ErrorDto.builder()
                .message(message)
                .build();
    }
}
