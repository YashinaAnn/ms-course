package com.learners.orderservice.controller;

import com.learners.orderservice.BaseTest;
import com.learners.orderservice.exception.CustomerNotFoundException;
import com.learners.orderservice.exception.OrderNotFoundException;
import com.learners.model.dto.order.OrderDto;
import com.learners.orderservice.service.OrderService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderControllerTest extends BaseTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private OrderService service;

    private static final UUID CUSTOMER_ID = UUID.randomUUID();
    private static final UUID ORDER_ID = UUID.randomUUID();
    private static final String BASE_URL = "/api/v1/customers/%s/orders";


    @BeforeAll
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetOrders() throws Exception {
        when(service.getOrders(CUSTOMER_ID, PageRequest.of(0, 10)))
                .thenReturn(mock(Page.class));

        mvc.perform(get(String.format(BASE_URL, CUSTOMER_ID)))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetOrders_NonExistingCustomerId() throws Exception {
        when(service.getOrders(CUSTOMER_ID, PageRequest.of(configs.getDefaultPage(), configs.getDefaultSize())))
                .thenThrow(new CustomerNotFoundException(CUSTOMER_ID));

        mvc.perform(get(String.format(BASE_URL, CUSTOMER_ID)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetOrderById() throws Exception {
        OrderDto orderDto = getValidOrderDto();

        when(service.getOrderById(CUSTOMER_ID, ORDER_ID))
                .thenReturn(orderDto);

        mvc.perform(get(String.format(BASE_URL, CUSTOMER_ID) + "/" + ORDER_ID))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetOrderById_NonExistingCustomerId() throws Exception {
        when(service.getOrderById(CUSTOMER_ID, ORDER_ID))
                .thenThrow(new CustomerNotFoundException(CUSTOMER_ID));

        mvc.perform(get(String.format(BASE_URL, CUSTOMER_ID) + "/" + ORDER_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetOrderById_NonExistingOrderId() throws Exception {
        when(service.getOrderById(CUSTOMER_ID, ORDER_ID))
                .thenThrow(new OrderNotFoundException(ORDER_ID));

        mvc.perform(get(String.format(BASE_URL, CUSTOMER_ID) + "/" + ORDER_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testPlaceOrder() throws Exception {
        OrderDto dto = getValidOrderDto();
        dto.setCustomerId(null);
        when(service.placeOrder(CUSTOMER_ID, dto)).thenReturn(dto);

        mvc.perform(post(String.format(BASE_URL, CUSTOMER_ID))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testPlaceOrder_NonExistingCustomerId() throws Exception {
        OrderDto dto = getValidOrderDto();
        dto.setCustomerId(null);

        when(service.placeOrder(CUSTOMER_ID, dto))
                .thenThrow(new CustomerNotFoundException(CUSTOMER_ID));

        mvc.perform(post(String.format(BASE_URL, CUSTOMER_ID))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }
}
