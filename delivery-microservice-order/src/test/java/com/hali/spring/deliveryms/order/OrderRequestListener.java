package com.hali.spring.deliveryms.order;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.hali.spring.deliveryms.model.events.ValidateOrderRequest;
import com.hali.spring.deliveryms.order.config.messaging.MessagingBeanConfig;

//@Component
public class OrderRequestListener 
{
//	@KafkaListener(topics = MessagingBeanConfig.ORDER_VALIDATE_QUEUE_REQUEST)
	public void listen(@Payload ValidateOrderRequest req)
	{
		
	}
}
