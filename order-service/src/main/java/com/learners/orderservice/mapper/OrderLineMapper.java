package com.learners.orderservice.mapper;

import com.learners.orderservice.entity.OrderLine;
import com.learners.model.dto.order.OrderLineDto;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
@DecoratedWith(OrderLineMapperDecorator.class)
public interface OrderLineMapper {

    @Mapping(source = "orderQty", target = "quantityOrdered")
    @Mapping(source = "qtyAllocated", target = "quantityAllocated")
    OrderLineDto orderLineToDto(OrderLine orderLine);
    @Mapping(source = "quantityOrdered", target = "orderQty")
    @Mapping(source = "quantityAllocated", target = "qtyAllocated")
    OrderLine dtoToOrderLine(OrderLineDto dto);
}