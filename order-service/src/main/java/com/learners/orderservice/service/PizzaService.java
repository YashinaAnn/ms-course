package com.learners.orderservice.service;

import com.learners.orderservice.model.dto.PizzaDto;
import com.learners.orderservice.model.dto.PizzaListDto;

public interface PizzaService {

    PizzaDto getPizzaById(Long id);
    PizzaListDto getPizzaList();
}
