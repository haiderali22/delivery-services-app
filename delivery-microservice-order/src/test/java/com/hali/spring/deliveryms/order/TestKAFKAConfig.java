package com.hali.spring.deliveryms.order;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.BytesDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.utils.Bytes;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.converter.Jackson2JavaTypeMapper.TypePrecedence;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import com.hali.spring.deliveryms.order.config.messaging.MessagingBeanConfig;

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
	
	public static Consumer<String, Bytes> configureConsumer(EmbeddedKafkaBroker embeddedKafkaBroker,String topic)
	{
		Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(MessagingBeanConfig.KAFKA_GROUP_ID, "true", embeddedKafkaBroker);
		consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, MessagingBeanConfig.KAFKA_GROUP_ID);
		consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, BytesDeserializer.class);
		consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		Consumer<String, Bytes> consumer = new DefaultKafkaConsumerFactory<String, Bytes>(consumerProps)
				.createConsumer();
		consumer.subscribe(Collections.singleton(topic));
		return consumer;
	}
}