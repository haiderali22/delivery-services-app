package com.hali.spring.deliveryms.order.config.messaging;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.BytesDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.utils.Bytes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class MessagingBeanConfig
{
	public static final String ORDER_VALIDATE_QUEUE_REQUEST = "order-validate-request";
	public static final String ORDER_VALIDATE_QUEUE_RESPONSE = "order-validate-reponse";
	public static final String ORDER_PAYMENT_QUEUE_REQUEST = "order-payment-request";
	public static final String ORDER_PAYMENT_QUEUE_RESPONSE = "order-payment-response";

	public static final String KAFKA_GROUP_ID = "delivery-ms";

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;


	@Bean
	public Map<String, Object> producerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return props;
	}

	//	@Bean
	//	public Map<String, Object> consumerConfigs() {
	//		Map<String, Object> props = new HashMap<>();
	//		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
	//		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
	//		props.put(ConsumerConfig.GROUP_ID_CONFIG, KAFKA_GROUP_ID);
	//		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, BytesDeserializer.class);
	//		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
	//		return props;
	//	}


	//	@Bean
	//	public NewTopic orderValidateTopic()
	//	{
	//		return new NewTopic(ORDER_VALIDATE_QUEUE_REQUEST, 1, (short) 1); 
	//	}	

	//	@Bean
	//	public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) 
	//	{
	//		KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory);
	//		kafkaTemplate.setMessageConverter(new StringJsonMessageConverter());
	//		return kafkaTemplate;
	//	}
	//
	@Bean
	public ConsumerFactory<String, Bytes> consumerFactory() {

		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
				StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, 
				BytesDeserializer.class);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, KAFKA_GROUP_ID);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		return new DefaultKafkaConsumerFactory<>(props);

	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Bytes> kafkaListenerContainerFactory() {

		ConcurrentKafkaListenerContainerFactory<String, Bytes> factory =
				new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		factory.setMessageConverter(new StringJsonMessageConverter());
		return factory;
	}

	@Bean
	public ProducerFactory<String, Object> producerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}

	//	@Bean
	//	public ConsumerFactory<String, Bytes> consumerFactory() {
	//		return new DefaultKafkaConsumerFactory<>(consumerConfigs());
	//	}

	@Bean
	public KafkaTemplate<String, Object> kafkaTemplate() {

		KafkaTemplate<String, Object> kafkaTemplate = new KafkaTemplate<>(producerFactory());
		kafkaTemplate.setMessageConverter(new StringJsonMessageConverter());
		return kafkaTemplate;
	}
}
