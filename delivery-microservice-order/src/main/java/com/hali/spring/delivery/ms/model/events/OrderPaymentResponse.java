package com.hali.spring.delivery.ms.model.events;

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
	private Long OrderId;
	private boolean recevied;
	
	private String reason;
}
