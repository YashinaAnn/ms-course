package com.learners.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PizzaDto implements Serializable {

    @Null
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String upc;
    @Positive
    private BigDecimal price;
    private PizzaType type;
    @Null
    private Integer inventoryOnHand;
}
