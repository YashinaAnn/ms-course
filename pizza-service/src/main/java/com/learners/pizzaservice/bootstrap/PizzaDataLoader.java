package com.learners.pizzaservice.bootstrap;

import com.learners.pizzaservice.entity.Pizza;
import com.learners.pizzaservice.model.PizzaType;
import com.learners.pizzaservice.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PizzaDataLoader implements CommandLineRunner {

    @Autowired
    private PizzaRepository pizzaRepository;

    public static final String UPC1 = "12312411";
    public static final String UPC2 = "22312412";
    public static final String UPC3 = "32312413";

    @Override
    public void run(String... args) {
        if (pizzaRepository.count() == 0) {
            loadData();
        }
    }

    public void loadData() {
        Pizza pizza1 = Pizza.builder()
                .upc(UPC1)
                .name("Super Pizza")
                .price(new BigDecimal("250.00"))
                .type(PizzaType.CHEEZ.name())
                .build();

        Pizza pizza2 = Pizza.builder()
                .upc(UPC2)
                .name("Sea Food Pizza")
                .price(new BigDecimal("300.00"))
                .type(PizzaType.SEA.name())
                .build();

        Pizza pizza3 = Pizza.builder()
                .upc(UPC3)
                .name("Margarita")
                .price(new BigDecimal("350.00"))
                .type(PizzaType.SOLYAMY.name())
                .build();

        pizzaRepository.save(pizza1);
        pizzaRepository.save(pizza2);
        pizzaRepository.save(pizza3);
    }
}
