package com.hali.spring.delivery.ms.model.events;

import com.hali.spring.delivery.ms.model.OrderDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderValidationRequest 
{
	private OrderDTO order;
}
