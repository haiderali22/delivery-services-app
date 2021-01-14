package com.hali.spring.deliveryms.catalog.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.hali.spring.deliveryms.catalog.config.messaging.MessagingBeanConfig;
import com.hali.spring.deliveryms.catalog.servcie.OrderValidator;
import com.hali.spring.deliveryms.model.ValidateOrderReponse;
import com.hali.spring.deliveryms.model.ValidateOrderRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderValidateRequestListener 
{
	private final OrderValidator orderValidator; 
	private final KafkaTemplate<String, Object> kafkaTemplate; 
	
	@KafkaListener(topics = MessagingBeanConfig.ORDER_VALIDATE_QUEUE_REQUEST,
			groupId = MessagingBeanConfig.KAFKA_GROUP_ID)
	public void orderValidate(Message<ValidateOrderRequest> requestMessage)
	{
		ValidateOrderRequest request = requestMessage.getPayload();
		
		boolean validOrder = orderValidator.validateOrder(request.getOrder());
		
		ValidateOrderReponse result =  ValidateOrderReponse.builder().
														OrderId(request.getOrder().getId()).valid(validOrder).build();
		
		kafkaTemplate.send(MessagingBeanConfig.ORDER_VALIDATE_QUEUE_RESPONSE, result);
	}
}
