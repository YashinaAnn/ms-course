package com.learners.orderservice.controller;

import com.learners.orderservice.BaseTest;
import com.learners.orderservice.config.AppConfigs;
import com.learners.orderservice.mapper.CustomerMapper;
import com.learners.orderservice.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class CustomerControllerTest extends BaseTest {

    private static final String BASE_URL = "/api/v1/customers";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private CustomerMapper mapper;
    @MockBean
    private CustomerService service;
    @Autowired
    private AppConfigs configs;

    @Test
    public void testGetCustomers() throws Exception {
        PageRequest pageRequest = PageRequest.of(configs.getDefaultPage(), configs.getDefaultSize());

        when(service.getCustomers(pageRequest)).thenReturn(Collections.emptyList());
        mvc.perform(get(BASE_URL)).andExpect(status().isOk());
    }
}