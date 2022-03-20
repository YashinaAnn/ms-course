package com.learners.pizzaservice.mapper;

import com.learners.pizzaservice.entity.Pizza;
import com.learners.model.dto.PizzaDto;
import com.learners.model.dto.PizzaListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PizzaListMapper {

    @Autowired
    @Qualifier("pizzaMapperImpl")
    private PizzaMapper pizzaMapper;

    public PizzaListDto pageToDto(Page<Pizza> page, boolean inventoryEnabled) {

        List<PizzaDto> data = page.getContent().stream()
                .map(pizza -> pizzaMapper.pizzaToDto(pizza, inventoryEnabled))
                .collect(Collectors.toList());

        return PizzaListDto.builder()
                .content(data)
                .pageNumber(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
