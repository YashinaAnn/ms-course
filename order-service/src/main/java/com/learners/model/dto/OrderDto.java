package com.learners.model.dto;

import com.learners.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    @Null
    private UUID id;
    @Null
    private UUID customerId;
    @NotEmpty
    private Set<OrderLineDto> orderLines;
    private OrderStatus orderStatus;
}
