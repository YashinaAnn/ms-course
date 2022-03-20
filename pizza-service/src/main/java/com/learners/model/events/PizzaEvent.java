package com.learners.model.events;

import com.learners.model.dto.PizzaDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PizzaEvent implements Serializable {

    private PizzaDto pizza;
}
