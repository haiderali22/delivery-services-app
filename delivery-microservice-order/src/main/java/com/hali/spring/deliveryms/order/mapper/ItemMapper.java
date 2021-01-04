package com.hali.spring.deliveryms.order.mapper;

import org.mapstruct.Mapper;

import com.hali.spring.deliveryms.model.ItemDto;
import com.hali.spring.deliveryms.order.domain.Item;

@Mapper
public interface ItemMapper 
{
	Item map(ItemDto order);
	ItemDto map(Item order);
}
