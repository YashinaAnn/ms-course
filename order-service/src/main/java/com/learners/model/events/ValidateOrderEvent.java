package com.learners.model.events;

import com.learners.model.dto.OrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidateOrderEvent {

    OrderDto order;

    public static ValidateOrderEvent of(OrderDto orderDto) {
        return ValidateOrderEvent.builder()
                .order(orderDto)
                .build();
    }
}
