package com.learners.inventoryfailover.service;

import com.learners.inventoryfailover.config.AppConfigs;
import com.learners.model.dto.order.InventoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryFailoverServiceImpl implements InventoryFailoverService {

    private final AppConfigs configs;

    @Override
    public List<InventoryDto> getDefaultInventory() {
        return List.of(InventoryDto.builder()
                .inventoryOnHand(configs.getDefaultInventory())
                .build());
    }
}
