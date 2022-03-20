package com.learners.orderservice.controller;

import com.learners.orderservice.config.AppConfigs;
import com.learners.orderservice.model.dto.OrderDto;
import com.learners.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers/{customerId}/orders")
public class OrderController {

    private final OrderService orderService;
    private final AppConfigs appConfigs;

    @GetMapping
    public ResponseEntity<Page<OrderDto>> getOrders(@PathVariable UUID customerId,
                                                    @RequestParam("page") Optional<Integer> pageOpt,
                                                    @RequestParam("size") Optional<Integer> sizeOpt) {
        int page = pageOpt.orElse(appConfigs.getDefaultPage());
        int size = sizeOpt.orElse(appConfigs.getDefaultSize());
        return ResponseEntity.ok(orderService.getOrders(customerId, PageRequest.of(page, size)));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable UUID customerId,
                                                 @PathVariable UUID orderId) {
        return ResponseEntity.ok(orderService.getOrderById(customerId, orderId));
    }

    @PostMapping
    public ResponseEntity<OrderDto> placeOrder(@PathVariable UUID customerId,
                                               @Validated @RequestBody OrderDto orderDto) {
        return new ResponseEntity<>(orderService.placeOrder(customerId, orderDto), HttpStatus.CREATED);
    }
}
