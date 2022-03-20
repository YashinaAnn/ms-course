package com.learners.pizzaservice.controller;

import com.learners.pizzaservice.config.AppsConfigs;
import com.learners.pizzaservice.model.PizzaDto;
import com.learners.pizzaservice.model.PizzaListDto;
import com.learners.pizzaservice.service.PizzaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/pizza")
@RequiredArgsConstructor
public class PizzaController {

    private final PizzaService service;
    private final AppsConfigs configs;

    @GetMapping
    public ResponseEntity<PizzaListDto> getAll(@RequestParam("page") Optional<Integer> pageOpt,
                                               @RequestParam("size") Optional<Integer> sizeOpt,
                                               @RequestParam("inventoryEnabled") Optional<Boolean> inventoryEnabledOpt) {

        int page = pageOpt.orElse(configs.getDefaultPage());
        int size = sizeOpt.orElse(configs.getDefaultSize());
        boolean inventoryEnabled = inventoryEnabledOpt.orElse(false);

        return new ResponseEntity<>(service.getAll(PageRequest.of(page, size), inventoryEnabled), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PizzaDto> getById(@PathVariable("id") long id,
                                            @RequestParam("inventoryEnabled") Optional<Boolean> inventoryEnabledOpt) {
        boolean inventoryEnabled = inventoryEnabledOpt.orElse(false);
        return new ResponseEntity<>(service.getPizzaById(id, inventoryEnabled), HttpStatus.OK);
    }

    @GetMapping("/upc/{upc}")
    public ResponseEntity<PizzaDto> getByUpc(@PathVariable("upc") String upc,
                                             @RequestParam("inventoryEnabled") Optional<Boolean> inventoryEnabledOpt) {
        boolean inventoryEnabled = inventoryEnabledOpt.orElse(false);
        return new ResponseEntity<>(service.getPizzaByUpc(upc, inventoryEnabled), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PizzaDto> createPizza(@Validated @RequestBody PizzaDto pizzaDto) {
        return new ResponseEntity<>(service.createPizza(pizzaDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PizzaDto> updatePizza(@PathVariable Long id,
                                                @Validated @RequestBody PizzaDto pizzaDto) {
        return new ResponseEntity<>(service.updatePizza(id, pizzaDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deletePizza(@PathVariable Long id) {
        service.deletePizza(id);
    }
}
