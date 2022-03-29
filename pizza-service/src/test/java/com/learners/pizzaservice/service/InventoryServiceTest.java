package com.learners.pizzaservice.service;

import com.learners.pizzaservice.BaseTest;
import com.learners.model.dto.inventory.InventoryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class InventoryServiceTest extends BaseTest {

    private static final Long PIZZA_ID = 1L;

    @MockBean
    private RestTemplate restTemplate;
    @Autowired
    private InventoryService service;

    @Test
    public void testGetInventory() {
        List<InventoryDto> list = new ArrayList<>();
        list.add(getInventoryDto(PIZZA_ID));
        list.add(getInventoryDto(PIZZA_ID));

        when(restTemplate.exchange(configs.getInventoryPath() + PIZZA_ID,
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<InventoryDto>>(){})
        ).thenReturn(ResponseEntity.ok(list));

        assertThat(service.getInventoryByPizzaId(PIZZA_ID).get())
                .isEqualTo(list.stream().map(InventoryDto::getInventoryOnHand).reduce(0, Integer::sum));
    }
}
