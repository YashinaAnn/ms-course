package com.learners.model.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryEvent implements Serializable {

    private long pizzaId;
    private String name;
    private int quantity;
}
