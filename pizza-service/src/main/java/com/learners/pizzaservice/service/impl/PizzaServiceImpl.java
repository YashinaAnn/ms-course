package com.learners.pizzaservice.service.impl;

import com.learners.pizzaservice.entity.Pizza;
import com.learners.pizzaservice.exception.PizzaNotFoundException;
import com.learners.pizzaservice.mapper.PizzaListMapper;
import com.learners.pizzaservice.mapper.PizzaMapper;
import com.learners.pizzaservice.model.PizzaDto;
import com.learners.pizzaservice.model.PizzaListDto;
import com.learners.pizzaservice.repository.PizzaRepository;
import com.learners.pizzaservice.service.PizzaService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PizzaServiceImpl implements PizzaService {

    private final PizzaRepository repository;
    private final PizzaListMapper pizzaListMapper;
    private final PizzaMapper pizzaMapper;

    @Cacheable(cacheNames = "pizzaListCache", condition = "#inventoryEnabled == false")
    @Override
    public PizzaListDto getAll(PageRequest pageRequest, boolean inventoryEnabled) {
        System.out.println("Executing method getAll");
        return pizzaListMapper.pageToDto(repository.findAll(pageRequest), inventoryEnabled);
    }

    @Cacheable(cacheNames = "pizzaByIdCache", key = "#id", condition = "#inventoryEnabled == false")
    @Override
    public PizzaDto getPizzaById(long id, boolean inventoryEnabled) {
        Pizza pizza = repository.findById(id).orElseThrow(() -> new PizzaNotFoundException(id));
        return pizzaMapper.pizzaToDto(pizza, inventoryEnabled);
    }

    @Cacheable(cacheNames = "pizzaByUpcCache", key = "#upc", condition = "#inventoryEnabled == false")
    @Override
    public PizzaDto getPizzaByUpc(String upc, boolean inventoryEnabled) {
        Pizza pizza = repository.findByUpc(upc).orElseThrow(() -> new PizzaNotFoundException(upc));
        return pizzaMapper.pizzaToDto(pizza, inventoryEnabled);
    }

    @Override
    public PizzaDto createPizza(PizzaDto dto) {
        Pizza pizza = repository.save(pizzaMapper.dtoToPizza(dto));
        return pizzaMapper.pizzaToDto(pizza);
    }

    @Override
    public PizzaDto updatePizza(long id, PizzaDto dto) {
        Pizza pizza = repository.findById(id).orElseThrow(() -> new PizzaNotFoundException(id));
        dto.setId(id);
        pizzaMapper.updatePizzaFromDto(dto, pizza);
        return pizzaMapper.pizzaToDto(repository.save(pizza));
    }

    @Override
    public void deletePizza(long id) {
        if (!repository.deletePizzaById(id)) {
            throw new PizzaNotFoundException(id);
        }
    }
}
