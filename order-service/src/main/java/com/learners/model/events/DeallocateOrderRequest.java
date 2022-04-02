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
public class DeallocateOrderRequest {

    private OrderDto order;

    public static DeallocateOrderRequest of(OrderDto order) {
        return DeallocateOrderRequest.builder()
                .order(order)
                .build();
    }
}
