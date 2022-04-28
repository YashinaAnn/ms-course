package com.learners.orderservice.controller;

import com.learners.model.dto.CustomerDto;
import com.learners.orderservice.config.AppConfigs;
import com.learners.orderservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final AppConfigs configs;

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getCustomers(@RequestParam("page") Optional<Integer> pageOpt,
                                                          @RequestParam("size") Optional<Integer> sizeOpt) {
        int page = pageOpt.orElse(configs.getDefaultPage());
        int size = sizeOpt.orElse(configs.getDefaultSize());
        return ResponseEntity.ok(customerService.getCustomers(PageRequest.of(page, size)));
    }
}
