package com.learners.inventoryservice.service;

import com.learners.inventoryservice.mapper.InventoryMapper;
import com.learners.model.dto.InventoryDto;
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
        return repository.findAllByPizzaId(id).stream()
                .map(mapper::inventoryToDto)
                .collect(Collectors.toList());
    }
}
