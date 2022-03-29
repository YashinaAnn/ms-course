package com.learners.model.events;

import com.learners.model.dto.order.OrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidateOrderRequest {

    OrderDto order;

    public static ValidateOrderRequest of(OrderDto orderDto) {
        return ValidateOrderRequest.builder()
                .order(orderDto)
                .build();
    }
}
