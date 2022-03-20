package com.learners.pizzaservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learners.pizzaservice.config.AppsConfigs;
import com.learners.pizzaservice.exception.PizzaNotFoundException;
import com.learners.pizzaservice.model.PizzaDto;
import com.learners.pizzaservice.model.PizzaListDto;
import com.learners.pizzaservice.model.PizzaType;
import com.learners.pizzaservice.service.PizzaService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles({"test"})
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PizzaControllerTest {

    private static final Long ID = 1L;
    private static final String UPC = "1234";
    private static final String BASE_URL = "/api/v1/pizza";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private PizzaService service;
    @Autowired
    private AppsConfigs configs;

    @BeforeAll
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAll() throws Exception {
        when(service.getAll(PageRequest.of(configs.getDefaultPage(), configs.getDefaultSize()), false))
                .thenReturn(getEmptyPizzaListDto());

        mvc.perform(get(BASE_URL))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetById() throws Exception {
        when(service.getPizzaById(ID, false)).thenReturn(getValidPizzaDto());

        mvc.perform(get(BASE_URL + "/" + ID))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetByUpc() throws Exception {
        when(service.getPizzaByUpc(UPC, false)).thenReturn(getValidPizzaDto());

        mvc.perform(get(BASE_URL + "/upc/" + UPC))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetByNonExistingId() throws Exception {
        when(service.getPizzaById(ID, false)).thenThrow(new PizzaNotFoundException(ID));

        mvc.perform(get(BASE_URL + "/" + ID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetByNonExistingUpc() throws Exception {
        when(service.getPizzaByUpc(UPC, false)).thenThrow(new PizzaNotFoundException());

        mvc.perform(get(BASE_URL + "/upc/" + UPC))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreatePizza() throws Exception {
        PizzaDto dto = getValidPizzaDto();
        when(service.createPizza(dto)).thenReturn(dto);

        mvc.perform(post(BASE_URL)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdatePizza() throws Exception {
        PizzaDto dto = getValidPizzaDto();
        when(service.updatePizza(ID, dto)).thenReturn(dto);

        mvc.perform(put(String.format("%s/%s", BASE_URL, ID))
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateNonExistingPizza() throws Exception {
        PizzaDto dto = getValidPizzaDto();
        when(service.updatePizza(ID, dto)).thenThrow(new PizzaNotFoundException(ID));

        mvc.perform(put(String.format("%s/%s", BASE_URL, ID))
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeletePizza() throws Exception {
        doNothing().when(service).deletePizza(ID);

        mvc.perform(delete(String.format("%s/%s", BASE_URL, ID)))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteNonExistingPizza() throws Exception {
        doThrow(new PizzaNotFoundException(ID)).when(service).deletePizza(ID);

        mvc.perform(delete(String.format("%s/%s", BASE_URL, ID)))
                .andExpect(status().isNotFound());
    }

    public PizzaDto getValidPizzaDto() {
        return PizzaDto.builder()
                .name("Pizza")
                .upc(UPC)
                .price(new BigDecimal("200"))
                .type(PizzaType.CHEEZ)
                .build();
    }

    public PizzaListDto getEmptyPizzaListDto() {
        return PizzaListDto.builder()
                .content(Collections.emptyList())
                .build();
    }
}
