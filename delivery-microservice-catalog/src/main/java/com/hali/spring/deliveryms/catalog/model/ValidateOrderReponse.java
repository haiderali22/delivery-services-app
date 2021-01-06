package com.hali.spring.deliveryms.catalog.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ValidateOrderReponse 
{
	private String OrderId;
	private boolean valid;
	
	private String reason;
}
