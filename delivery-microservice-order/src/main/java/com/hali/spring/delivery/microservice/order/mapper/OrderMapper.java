package com.hali.spring.delivery.microservice.order.mapper;

import org.mapstruct.Mapper;

import com.hali.spring.delivery.microservice.order.domain.Order;
import com.hali.spring.delivery.ms.model.OrderDTO;

@Mapper
public interface OrderMapper 
{
	Order fromDTO(OrderDTO order);
	OrderDTO toDTO(Order order);
}
