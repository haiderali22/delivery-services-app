package com.hali.spring.delivery.ms.catalog.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.hali.spring.delivery.ms.catalog.config.communication.CommunicationBeanConfig;
import com.hali.spring.delivery.ms.model.OrderValidateResponse;
import com.hali.spring.delivery.ms.model.OrderValidationRequest;

@Component
public class OrderValidateRequestListener 
{

	@KafkaListener(topics = CommunicationBeanConfig.ORDER_VALIDATE_QUEUE_REQUEST)
	public void orderValidate(@Payload OrderValidationRequest request)
	{
		
	}
}
