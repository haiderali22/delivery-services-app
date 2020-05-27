package com.hali.spring.delivery.microservice.order.listeners;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.hali.spring.delivery.microservice.order.config.jms.CommunicationBeanConfig;
import com.hali.spring.delivery.ms.model.events.OrderValidateResponse;

@Component
public class OrderValidateListener 
{

	@KafkaListener(topics = CommunicationBeanConfig.ORDER_VALIDATE_QUEUE_RESPONSE)
	public void orderValidate(@Payload OrderValidateResponse response)
	{
		
	}
}
