package com.learners.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PizzaListDto {

    private long totalElements;
    private int totalPages;
    private int size;
    private int pageNumber;
    private List<PizzaDto> content;
}
