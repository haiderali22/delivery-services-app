package com.hali.spring.deliveryms.model.events;

import java.io.Serializable;

import com.hali.spring.deliveryms.model.OrderDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidateOrderRequest implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3613948632949550376L;
	
	private OrderDto order;
}
