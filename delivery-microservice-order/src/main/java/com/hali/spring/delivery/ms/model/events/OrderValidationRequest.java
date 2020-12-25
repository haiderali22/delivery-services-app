package com.hali.spring.delivery.ms.model.events;

import java.io.Serializable;

import com.hali.spring.delivery.ms.model.OrderDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderValidationRequest implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3613948632949550376L;
	
	private OrderDto order;
}
