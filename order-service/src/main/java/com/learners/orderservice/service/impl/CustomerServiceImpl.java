package com.learners.orderservice.service.impl;

import com.learners.model.dto.CustomerDto;
import com.learners.orderservice.mapper.CustomerMapper;
import com.learners.orderservice.repository.CustomerRepository;
import com.learners.orderservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper mapper;

    @Override
    public List<CustomerDto> getCustomers(PageRequest pageRequest) {
        return customerRepository.findAll(pageRequest).stream()
                .map(mapper::customerToDto)
                .collect(Collectors.toList());
    }
}
