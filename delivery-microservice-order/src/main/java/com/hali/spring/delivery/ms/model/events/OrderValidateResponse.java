package com.hali.spring.delivery.ms.model.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderValidateResponse 
{
	private Long OrderId;
	private boolean validated;
	
	private String reason;
}
