package com.learners.inventoryservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learners.inventoryservice.exception.PizzaNotFoundException;
import com.learners.inventoryservice.service.InventoryService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ActiveProfiles({"test"})
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InventoryControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private InventoryService service;

    private static final Long ID = 1L;

    @BeforeAll
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllByPizzaId() throws Exception {
        when(service.getByPizzaId(ID)).thenReturn(any());

        mvc.perform(get("/api/v1/inventory/" + ID))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllByNonExistingPizzaId() throws Exception {
        when(service.getByPizzaId(ID)).thenThrow(new PizzaNotFoundException());

        mvc.perform(get("/api/v1/inventory/" + ID))
                .andExpect(status().isBadRequest());
    }
}
