package com.learners.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDto {

    private String message;

    public static ErrorDto of(String message) {
        return ErrorDto.builder()
                .message(message)
                .build();
    }
}
