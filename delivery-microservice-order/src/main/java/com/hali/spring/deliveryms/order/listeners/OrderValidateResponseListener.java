package com.hali.spring.deliveryms.order.listeners;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.hali.spring.deliveryms.model.events.OrderValidateResponse;
import com.hali.spring.deliveryms.order.config.messaging.MessagingBeanConfig;
import com.hali.spring.deliveryms.order.services.OrderManager;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderValidateResponseListener 
{
	private final OrderManager orderManager;
	
	@KafkaListener(topics = MessagingBeanConfig.ORDER_VALIDATE_QUEUE_RESPONSE,
			groupId = "order-delivery")
	public void orderValidate(Message<OrderValidateResponse>   response) throws Exception
	{
		orderManager.processValidationResponse(response.getPayload());
	}
}
