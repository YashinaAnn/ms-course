package com.learners.pizzaservice;

import com.learners.pizzaservice.config.AppsConfigs;
import com.learners.pizzaservice.entity.Pizza;
import com.learners.pizzaservice.mapper.PizzaMapper;
import com.learners.pizzaservice.model.InventoryDto;
import com.learners.pizzaservice.model.PizzaDto;
import com.learners.pizzaservice.model.PizzaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.UUID;

@ActiveProfiles({"test"})
@SpringBootTest
public class BaseTest {

    public static final int MOCKED_INVENTORY = 10;

    @Autowired
    protected AppsConfigs configs;
    @Autowired
    @Qualifier("pizzaMapperImpl")
    protected PizzaMapper mapper;

    protected Pizza getValidPizza() {
        return Pizza.builder()
                .name("Pizza")
                .price(new BigDecimal("200.00"))
                .type(PizzaType.CHEEZ.name())
                .build();
    }

    protected PizzaDto getValidPizzaDto() {
        return PizzaDto.builder()
                .name("New Pizza")
                .price(new BigDecimal("400.00"))
                .type(PizzaType.PEPERONNY)
                .build();
    }

    public InventoryDto getInventoryDto(long pizzaId) {
        return InventoryDto.builder()
                .id(UUID.randomUUID())
                .pizzaId(pizzaId)
                .inventoryOnHand(MOCKED_INVENTORY)
                .build();
    }
}
