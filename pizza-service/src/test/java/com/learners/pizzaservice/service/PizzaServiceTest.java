package com.learners.pizzaservice.service;

import com.learners.pizzaservice.BaseTest;
import com.learners.pizzaservice.entity.Pizza;
import com.learners.model.dto.PizzaDto;
import com.learners.pizzaservice.repository.PizzaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PizzaServiceTest extends BaseTest {

    @Autowired
    private PizzaRepository repository;
    @Autowired
    private PizzaService service;

    @Test
    public void testUpdate() {
        Pizza pizza = repository.save(getValidPizza());
        PizzaDto newPizza = getValidPizzaDto();

        PizzaDto response = service.updatePizza(pizza.getId(), newPizza);

        newPizza.setId(pizza.getId());
        Assertions.assertEquals(newPizza, response);

        Pizza expectedPizza = repository.findById(pizza.getId()).get();
        Pizza actualPizza = mapper.dtoToPizza(newPizza);
        actualPizza.setVersion(expectedPizza.getVersion());
        actualPizza.setCreationDate(expectedPizza.getCreationDate());
        actualPizza.setLastModifiedDate(expectedPizza.getLastModifiedDate());

        Assertions.assertEquals(actualPizza, expectedPizza);
    }
}