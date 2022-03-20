package com.learners.inventoryservice.mapper;

import com.learners.inventoryservice.domain.Inventory;
import com.learners.inventoryservice.model.dto.InventoryDto;
import org.mapstruct.Mapper;

@Mapper
public interface InventoryMapper {

    Inventory dtoToInventory(InventoryDto dto);
    InventoryDto inventoryToDto(Inventory inventory);
}
