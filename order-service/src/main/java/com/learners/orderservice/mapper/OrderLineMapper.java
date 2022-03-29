package com.learners.orderservice.mapper;

import com.learners.orderservice.entity.OrderLine;
import com.learners.model.dto.OrderLineDto;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
@DecoratedWith(OrderLineMapperDecorator.class)
public interface OrderLineMapper {

    @Mapping(source = "orderQty", target = "quantity")
    OrderLineDto orderLineToDto(OrderLine orderLine);
    @Mapping(source = "quantity", target = "orderQty")
    OrderLine dtoToOrderLine(OrderLineDto dto);
}