package com.hali.spring.delivery.ms.model;

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
