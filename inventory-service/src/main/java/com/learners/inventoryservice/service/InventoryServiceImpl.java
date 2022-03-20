package com.learners.inventoryservice.service;

import com.learners.inventoryservice.domain.Inventory;
import com.learners.inventoryservice.exception.PizzaNotFoundException;
import com.learners.inventoryservice.mapper.InventoryMapper;
import com.learners.inventoryservice.model.dto.InventoryDto;
import com.learners.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository repository;
    private final InventoryMapper mapper;

    @Override
    public List<InventoryDto> getByPizzaId(Long id) {
        List<Inventory> inventories = repository.findAllByPizzaId(id);
        if (inventories.isEmpty()) {
            throw new PizzaNotFoundException(id);
        }
        return inventories.stream()
                .map(mapper::inventoryToDto)
                .collect(Collectors.toList());
    }
}
