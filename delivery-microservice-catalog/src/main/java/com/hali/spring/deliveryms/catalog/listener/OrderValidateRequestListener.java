package com.hali.spring.deliveryms.catalog.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.hali.spring.deliveryms.catalog.config.messaging.CommunicationBeanConfig;
import com.hali.spring.deliveryms.catalog.model.OrderValidateResponse;
import com.hali.spring.deliveryms.catalog.model.OrderValidationRequest;

@Component
public class OrderValidateRequestListener 
{

	@KafkaListener(topics = CommunicationBeanConfig.ORDER_VALIDATE_QUEUE_REQUEST)
	public void orderValidate(@Payload OrderValidationRequest request)
	{
		
	}
}
