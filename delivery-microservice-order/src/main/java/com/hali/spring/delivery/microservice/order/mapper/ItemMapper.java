package com.hali.spring.delivery.microservice.order.mapper;

import org.mapstruct.Mapper;

import com.hali.spring.delivery.microservice.order.domain.Item;
import com.hali.spring.delivery.ms.model.ItemDto;

@Mapper
public interface ItemMapper 
{
	Item map(ItemDto order);
	ItemDto map(Item order);
}
