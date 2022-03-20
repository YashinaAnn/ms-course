package com.learners.pizzaservice.integration;

import com.learners.pizzaservice.BaseTest;
import com.learners.pizzaservice.controller.PizzaController;
import com.learners.pizzaservice.entity.Pizza;
import com.learners.pizzaservice.mapper.PizzaMapper;
import com.learners.pizzaservice.model.InventoryDto;
import com.learners.pizzaservice.model.PizzaDto;
import com.learners.pizzaservice.model.PizzaListDto;
import com.learners.pizzaservice.repository.PizzaRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ActiveProfiles({"test"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PizzaIntegrationTest extends BaseTest {

    @MockBean
    RestTemplate restTemplate;
    @Autowired
    private PizzaRepository repository;
    @Autowired
    private PizzaController controller;
    @Autowired
    @Qualifier("pizzaMapperImpl")
    private PizzaMapper mapper;

    private Pizza pizza1;
    private Pizza pizza2;

    @BeforeAll
    public void loadData() {
        pizza1 = repository.save(getValidPizza());
        pizza2 = repository.save(getValidPizza());
    }

    @Test
    public void testGetAll_DefaultPagination() {
        ResponseEntity<PizzaListDto> response = controller.getAll(Optional.empty(), Optional.empty(), Optional.empty());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).contains(mapper.pizzaToDto(pizza1), mapper.pizzaToDto(pizza2));

        assertThat(response.getBody().getPageNumber()).isEqualTo(configs.getDefaultPage());
        assertThat(response.getBody().getSize()).isEqualTo(configs.getDefaultSize());
    }

    @Test
    public void testGetAll_FirstPage() {
        ResponseEntity<PizzaListDto> response = controller.getAll(Optional.of(0), Optional.of(1), Optional.empty());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).hasSize(1);
        assertThat(response.getBody().getContent()).contains(mapper.pizzaToDto(pizza1));
    }

    @Test
    public void testGetAll_SecondPage() {
        ResponseEntity<PizzaListDto> response = controller.getAll(Optional.of(1), Optional.of(1), Optional.empty());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).hasSize(1);
        assertThat(response.getBody().getContent()).contains(mapper.pizzaToDto(pizza2));
    }

    @Test
    public void testGetAll_WithoutInventory() {
        ResponseEntity<PizzaListDto> response = controller.getAll(Optional.empty(), Optional.empty(), Optional.empty());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        List<PizzaDto> list = response.getBody().getContent().stream()
                .filter(x -> null != x.getInventoryOnHand())
                .collect(Collectors.toList());

        assertThat(list).isEmpty();
    }

    @Test
    public void testGetAll_WithInventory() {
        mockInventory(pizza1.getId());
        mockInventory(pizza2.getId());

        ResponseEntity<PizzaListDto> response = controller.getAll(Optional.empty(), Optional.empty(), Optional.of(true));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        List<PizzaDto> list = response.getBody().getContent().stream()
                .filter(x -> x.getInventoryOnHand() == null || x.getInventoryOnHand() != MOCKED_INVENTORY)
                .collect(Collectors.toList());

        assertThat(list).isEmpty();
    }

    @Test
    public void testGetById_WithoutInventory() {
        ResponseEntity<PizzaDto> response = controller.getById(pizza1.getId(), Optional.empty());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getInventoryOnHand()).isNull();
    }

    @Test
    public void testGetById_WithInventory() {
        mockInventory(pizza1.getId());

        ResponseEntity<PizzaDto> response = controller.getById(pizza1.getId(), Optional.of(true));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getInventoryOnHand()).isEqualTo(MOCKED_INVENTORY);
    }

    @Test
    public void testGetById_WithInventory_NoInventory() {
        when(restTemplate.exchange(configs.getInventoryPath() + pizza1.getId(),
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<InventoryDto>>(){})
        ).thenReturn(ResponseEntity.ok(Collections.emptyList()));

        ResponseEntity<PizzaDto> response = controller.getById(pizza1.getId(), Optional.of(true));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getInventoryOnHand()).isEqualTo(0);
    }

    private void mockInventory(long pizzaId) {
        when(restTemplate.exchange(configs.getInventoryPath() + pizzaId,
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<InventoryDto>>(){})
        ).thenReturn(ResponseEntity.ok(Collections.singletonList(getInventoryDto(pizzaId))));
    }
}
