package com.hali.spring.delivery.microservice.order.config.jms;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.internals.Topic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommunicationBeanConfig
{
	public static final String ORDER_VALIDATE_QUEUE = "order-validate";
	public static final String ORDER_VALIDATE_QUEUE_RESPONSE = "order-validate-reponse";
	
	@Bean
	public NewTopic orderValidateTopic()
	{
		return new NewTopic(ORDER_VALIDATE_QUEUE, 1, (short) 1); 
	}	
}
