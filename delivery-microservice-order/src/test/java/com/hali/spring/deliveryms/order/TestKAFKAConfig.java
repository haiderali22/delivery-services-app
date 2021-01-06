package com.hali.spring.deliveryms.order;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.converter.Jackson2JavaTypeMapper.TypePrecedence;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

@TestConfiguration
public class TestKAFKAConfig {

//	@Bean
//	@Primary
	public ProducerFactory<String, Object> producerFactory(EmbeddedKafkaBroker embeddedKafkaBroker) {
		
		Map<String, Object> props = KafkaTestUtils.producerProps(embeddedKafkaBroker);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		
		return new DefaultKafkaProducerFactory<>(props);
	}

	@Bean
	@Primary
	public KafkaTemplate<String, Object> kafkaTemplate(EmbeddedKafkaBroker embeddedKafkaBroker) 
	{
		KafkaTemplate<String, Object> kafkaTemplate = new KafkaTemplate<String, Object>(producerFactory(embeddedKafkaBroker));
		
//		StringJsonMessageConverter converter = new StringJsonMessageConverter();
//		
//		DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
//		typeMapper.setTypePrecedence(TypePrecedence.TYPE_ID);
//		converter.setTypeMapper(typeMapper);
		
		kafkaTemplate.setMessageConverter( new StringJsonMessageConverter());
		
		return kafkaTemplate;
	}
}