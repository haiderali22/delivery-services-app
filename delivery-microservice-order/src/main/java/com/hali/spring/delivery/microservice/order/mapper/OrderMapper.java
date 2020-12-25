package com.hali.spring.delivery.microservice.order.mapper;

import org.mapstruct.Mapper;

import com.hali.spring.delivery.microservice.order.domain.Order;
import com.hali.spring.delivery.ms.model.OrderDto;

@Mapper
public interface OrderMapper 
{
	Order map(OrderDto order);
	OrderDto map(Order order);
}
