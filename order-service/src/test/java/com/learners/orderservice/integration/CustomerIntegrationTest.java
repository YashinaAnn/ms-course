package com.learners.orderservice.integration;

import com.learners.model.dto.CustomerDto;
import com.learners.orderservice.BaseTest;
import com.learners.orderservice.controller.CustomerController;
import com.learners.orderservice.entity.Customer;
import com.learners.orderservice.mapper.CustomerMapper;
import com.learners.orderservice.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerIntegrationTest extends BaseTest {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerController customerController;
    @Autowired
    private CustomerMapper mapper;

    @Transactional
    @Test
    public void testGetCustomers() {
        Customer customer1 = customerRepository.save(getValidCustomer());
        Customer customer2 = customerRepository.save(getValidCustomer());

        ResponseEntity<List<CustomerDto>> response = customerController.getCustomers(Optional.empty(), Optional.empty());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).contains(mapper.customerToDto(customer1), mapper.customerToDto(customer2));
    }

    @Transactional
    @Test
    public void testGetCustomers_PaginationFirstPage() {
        Customer customer1 = customerRepository.save(getValidCustomer());
        Customer customer2 = customerRepository.save(getValidCustomer());

        ResponseEntity<List<CustomerDto>> response = customerController.getCustomers(Optional.of(0), Optional.of(1));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(1);
        assertThat(response.getBody()).contains(mapper.customerToDto(customer1));
        assertThat(response.getBody()).doesNotContain(mapper.customerToDto(customer2));
    }

    @Transactional
    @Test
    public void testGetCustomers_PaginationSecondPage() {
        Customer customer1 = customerRepository.save(getValidCustomer());
        Customer customer2 = customerRepository.save(getValidCustomer());

        ResponseEntity<List<CustomerDto>> response = customerController.getCustomers(Optional.of(1), Optional.of(1));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(1);
        assertThat(response.getBody()).contains(mapper.customerToDto(customer2));
        assertThat(response.getBody()).doesNotContain(mapper.customerToDto(customer1));
    }
}
