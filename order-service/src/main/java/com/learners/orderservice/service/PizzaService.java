package com.learners.orderservice.service;

import com.learners.orderservice.model.dto.PizzaDto;

public interface PizzaService {

    PizzaDto getPizzaById(Long id);
}
