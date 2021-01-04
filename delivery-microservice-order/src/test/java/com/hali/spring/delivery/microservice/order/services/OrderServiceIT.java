package com.hali.spring.delivery.microservice.order.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.awaitility.Awaitility;
import org.awaitility.Durations;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hali.spring.deliveryms.model.OrderDto;
import com.hali.spring.deliveryms.model.events.OrderPaymentResponse;
import com.hali.spring.deliveryms.model.events.OrderValidateResponse;
import com.hali.spring.deliveryms.order.config.messaging.MessagingBeanConfig;
import com.hali.spring.deliveryms.order.domain.Order;
import com.hali.spring.deliveryms.order.domain.OrderState;
import com.hali.spring.deliveryms.order.repositories.OrderRepository;
import com.hali.spring.deliveryms.order.services.OrderService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@EmbeddedKafka(partitions = 1,bootstrapServersProperty = "${spring.kafka.bootstrap-servers}",
		topics = {MessagingBeanConfig.
		ORDER_VALIDATE_QUEUE_REQUEST,MessagingBeanConfig.ORDER_VALIDATE_QUEUE_RESPONSE})
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderServiceIT 
{
	
	@Autowired
	private OrderService service;

	@Autowired
	private EmbeddedKafkaBroker embeddedKafkaBroker;

	@Autowired
	ObjectMapper objMapper;
	
	@Autowired
	OrderRepository orderRepo;
	
	OrderDto order;
	
	String cartId;

	//	private KafkaMessageListenerContainer<String, Object> container;
	//
	//    private BlockingQueue<ConsumerRecord<String, String>> consumerRecords;

	@BeforeEach
	void setUp() throws Exception 
	{
		cartId  = "1234456";
//		 System.setProperty("spring.kafka.bootstrap-servers", embeddedKafkaBroker.getBrokersAsString());
		order = new OrderDto();
//		order.setCurrentState(OrderState.NEW);

	}
	
//	@BeforeAll
	void setUpAll() 
	{
		//        Map<String, Object> configs = new HashMap<>(KafkaTestUtils.consumerProps("order-delivery", "false", embeddedKafkaBroker));
		//        DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), new StringDeserializer());
		//        ContainerProperties containerProperties = 
		//        		new ContainerProperties(MessagingBeanConfig.ORDER_VALIDATE_QUEUE_REQUEST);
		//        container = new KafkaMessageListenerContainer(consumerFactory, containerProperties);
		//        consumerRecords = new LinkedBlockingQueue<>();
		//        container.setupMessageListener((MessageListener<String, String>) consumerRecords::add);
		//        container.start();
		//        ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());
	}

//	@AfterAll
	void tearDown() 
	{
		//        container.stop();
	}

	@Test
	@Transactional
	public void testValidationRequest() throws Exception
	{
		//    	 Map<String, Object> configs = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
		//         Producer<String, String> producer = new DefaultKafkaProducerFactory<>(configs, new StringSerializer(), new StringSerializer()).createProducer();
		//
		OrderValidateResponse resp = OrderValidateResponse.builder().OrderId(1L).validated(false).build();
		//         
		//         
		//         producer.send(new ProducerRecord<>(MessagingBeanConfig.
		//        		 ORDER_VALIDATE_QUEUE_REQUEST, "my-evt-id", objMapper.writeValueAsString(resp)));
		//         producer.flush();
		//
		//         
		//         ConsumerRecord<String, String> singleRecord = consumerRecords.poll(100, TimeUnit.MILLISECONDS);
		//         assertThat(singleRecord).isNotNull();

		
		
		Consumer<String, String> consumer = configureConsumer(MessagingBeanConfig.ORDER_VALIDATE_QUEUE_REQUEST);
//		Producer<Integer, String> producer = configureProducer();
//
//		producer.send(new ProducerRecord<>(MessagingBeanConfig.ORDER_VALIDATE_QUEUE_REQUEST, 
//				123,  objMapper.writeValueAsString(resp)));
		
		OrderDto saved = service.createOrder(cartId, order);

		ConsumerRecord<String, String> singleRecord = KafkaTestUtils.getSingleRecord(consumer, 
				MessagingBeanConfig.ORDER_VALIDATE_QUEUE_REQUEST);
		
	
		
		assertThat(singleRecord).isNotNull();
		//        assertThat(singleRecord.key()).isEqualTo(123);
		//        assertThat(singleRecord.value()).isEqualTo(objMapper.writeValueAsString(resp));
		System.out.println("Log Listener request " + singleRecord.value());
		
//		Producer<String, String> producer = configureProducer();
//
//		producer.send(new ProducerRecord<>(MessagingBeanConfig.ORDER_VALIDATE_QUEUE_REQUEST, 
//				"123",  objMapper.writeValueAsString(resp)));
		
		
		
		consumer.close();
//		producer.close();	
	}
	
	@Test
	public void testValidationInvalidResponse() throws Exception
	{
		
		
//		Consumer<String, String> consumer = configureConsumer(MessagingBeanConfig.ORDER_VALIDATE_QUEUE_REQUEST);
//		Producer<Integer, String> producer = configureProducer();
//
//		producer.send(new ProducerRecord<>(MessagingBeanConfig.ORDER_VALIDATE_QUEUE_REQUEST, 
//				123,  objMapper.writeValueAsString(resp)));
		
		OrderDto saved = service.createOrder(cartId, order);
		
		OrderValidateResponse resp = OrderValidateResponse.builder().
				OrderId(saved.getId()).validated(false).build();				
		

//		ConsumerRecord<String, String> singleRecord = KafkaTestUtils.getSingleRecord(consumer, 
//				MessagingBeanConfig.ORDER_VALIDATE_QUEUE_REQUEST);
//		
//	
//		
//		assertThat(singleRecord).isNotNull();
		//        assertThat(singleRecord.key()).isEqualTo(123);
		//        assertThat(singleRecord.value()).isEqualTo(objMapper.writeValueAsString(resp));
//		System.out.println("Log Listener request " + singleRecord.value());
		
		Producer<Integer, Object> producer = configureProducer();
//
		producer.send(new ProducerRecord<>(MessagingBeanConfig.ORDER_VALIDATE_QUEUE_RESPONSE, 
				123,  resp));
		
		
		
		Awaitility.await().atMost(Durations.FIVE_SECONDS).untilAsserted(()->{
			Order fetchOrder =  orderRepo.findById(saved.getId()).get();

			Assertions.assertEquals(OrderState.VALIDATION_FAILED, fetchOrder.getCurrentState());
		});
		
//		consumer.close();
		producer.close();	
	}
	
	@Test
	public void testValidationValidResponse() throws Exception
	{		
						
		
		OrderDto saved = service.createOrder(cartId, order);
		Producer<Integer, Object> producer = configureProducer();

		OrderValidateResponse resp = OrderValidateResponse.builder()
					.OrderId(saved.getId())
					.validated(true).build();
		
		producer.send(new ProducerRecord<>(MessagingBeanConfig.ORDER_VALIDATE_QUEUE_RESPONSE, 
				123,  resp));		
		
		Awaitility.await().atMost(Durations.FIVE_SECONDS).untilAsserted(()->{
			Order fetchOrder =  orderRepo.findById(saved.getId()).get();

			Assertions.assertEquals(OrderState.READY_FOR_DELIVERY, fetchOrder.getCurrentState());
		});
		
		producer.close();	
	}
	
	@Test
	public void testNewToReadyForDeliveryPrePaid() throws Exception
	{		
		order.setPrePaid(true);		
		
		OrderDto saved = service.createOrder(cartId, order);
		Producer<Integer, Object> producer = configureProducer();
		
		OrderValidateResponse resp = OrderValidateResponse.builder().
				OrderId(saved.getId()).validated(true).build();		

		producer.send(new ProducerRecord<>(MessagingBeanConfig.ORDER_VALIDATE_QUEUE_RESPONSE, 
				123,  resp));
		
		Awaitility.await().atMost(Durations.FIVE_SECONDS).untilAsserted(()->{
			Order fetchOrder =  orderRepo.findById(saved.getId()).get();

			Assertions.assertEquals(OrderState.AWAITING_PAYMENT, fetchOrder.getCurrentState());
		});
		
		OrderPaymentResponse payRes = OrderPaymentResponse.builder().
				OrderId(saved.getId()).recevied(true).build();	
		
		producer.send(new ProducerRecord<>(MessagingBeanConfig.ORDER_PAYMENT_QUEUE_RESPONSE, 
				123,  payRes));		
		
		Awaitility.await().atMost(Durations.FIVE_SECONDS).untilAsserted(()->{
			Order fetchOrder =  orderRepo.findById(saved.getId()).get();

			Assertions.assertEquals(OrderState.READY_FOR_DELIVERY, fetchOrder.getCurrentState());
		});
		
		producer.close();	
	}

	private Consumer<String, String> configureConsumer(String topic) {
		Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("order-delivery", "true", embeddedKafkaBroker);
		consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		Consumer<String, String> consumer = new DefaultKafkaConsumerFactory<String, String>(consumerProps)
				.createConsumer();
		consumer.subscribe(Collections.singleton(topic));
		return consumer;
	}

	private Producer<Integer,Object> configureProducer() {
		Map<String, Object> producerProps = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
		
		producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return new DefaultKafkaProducerFactory<Integer, Object>(producerProps).createProducer();
	}
}
