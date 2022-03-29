package com.learners.model.dto.order;

import com.learners.model.dto.PizzaType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineDto {

    @Null
    private UUID id;
    @NotNull
    private Long pizzaId;
    @NotBlank
    private String upc;
    @NotBlank
    private String pizzaName;
    @NotNull
    private PizzaType type;
    @Positive
    private int quantityOrdered;
    private int quantityAllocated;
}
