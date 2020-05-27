package com.hali.spring.delivery.ms.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderLineDTO 
{
	private Long productId;
	private int amount;
}
