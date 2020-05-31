package com.hali.spring.delivery.microservice.order;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.hali.spring.delivery.microservice.order.config.communication.CommunicationBeanConfig;
import com.hali.spring.delivery.ms.model.events.OrderValidationRequest;

//@Component
public class OrderRequestListener 
{
//	@KafkaListener(topics = CommunicationBeanConfig.ORDER_VALIDATE_QUEUE_REQUEST)
	public void listen(@Payload OrderValidationRequest req)
	{
		
	}
}
