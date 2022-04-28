package com.learners.orderservice.service;

import com.learners.model.dto.CustomerDto;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface CustomerService {

    List<CustomerDto> getCustomers(PageRequest pageRequest);
}
