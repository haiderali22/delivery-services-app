package com.hali.spring.deliveryms.catalog.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.hali.spring.deliveryms.catalog.config.messaging.MessagingBeanConfig;
import com.hali.spring.deliveryms.catalog.model.ValidateOrderReponse;
import com.hali.spring.deliveryms.catalog.model.ValidateOrderRequest;
import com.hali.spring.deliveryms.catalog.servcie.OrderValidator;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderValidateRequestListener 
{
	private final OrderValidator orderValidator; 
	
	@KafkaListener(topics = MessagingBeanConfig.ORDER_VALIDATE_QUEUE_REQUEST)
	public void orderValidate(@Payload ValidateOrderRequest request)
	{
		boolean validOrder = orderValidator.validateOrder(request.getOrder());
		
		ValidateOrderReponse result =  ValidateOrderReponse.builder().
														OrderId(request.getOrder().getId()).valid(validOrder).build();
		
		
	}
}
