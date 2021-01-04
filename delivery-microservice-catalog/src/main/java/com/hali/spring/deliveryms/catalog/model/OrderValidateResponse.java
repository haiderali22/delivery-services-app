package com.hali.spring.deliveryms.catalog.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderValidateResponse 
{
	private Long OrderId;
	private boolean validated;
	
	private String reason;
}
