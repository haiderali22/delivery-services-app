package com.hali.spring.deliveryms.model.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderPaymentResponse 
{
	private String OrderId;
	private boolean recevied;
	
	private String reason;
}
