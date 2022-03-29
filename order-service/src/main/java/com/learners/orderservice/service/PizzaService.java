package com.learners.orderservice.service;

import com.learners.model.dto.PizzaDto;
import com.learners.model.dto.PizzaListDto;

public interface PizzaService {

    PizzaDto getPizzaById(Long id);
    PizzaListDto getPizzaList();
}
