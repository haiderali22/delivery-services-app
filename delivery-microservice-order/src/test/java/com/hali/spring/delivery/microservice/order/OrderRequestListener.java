package com.hali.spring.delivery.microservice.order;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.hali.spring.deliveryms.model.events.OrderValidationRequest;
import com.hali.spring.deliveryms.order.config.messaging.MessagingBeanConfig;

//@Component
public class OrderRequestListener 
{
//	@KafkaListener(topics = MessagingBeanConfig.ORDER_VALIDATE_QUEUE_REQUEST)
	public void listen(@Payload OrderValidationRequest req)
	{
		
	}
}
