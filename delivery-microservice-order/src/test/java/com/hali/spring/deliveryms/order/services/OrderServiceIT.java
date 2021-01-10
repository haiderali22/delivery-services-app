package com.hali.spring.deliveryms.order.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
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
import org.apache.kafka.common.serialization.BytesDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.utils.Bytes;
import org.awaitility.Awaitility;
import org.awaitility.Durations;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.geo.Point;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
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
import com.hali.spring.deliveryms.model.ItemDto;
import com.hali.spring.deliveryms.model.OrderDto;
import com.hali.spring.deliveryms.model.ProductDto;
import com.hali.spring.deliveryms.model.events.OrderPaymentResponse;
import com.hali.spring.deliveryms.model.events.ValidateOrderReponse;
import com.hali.spring.deliveryms.order.TestKAFKAConfig;
import com.hali.spring.deliveryms.order.config.messaging.MessagingBeanConfig;
import com.hali.spring.deliveryms.order.domain.Item;
import com.hali.spring.deliveryms.order.domain.Order;
import com.hali.spring.deliveryms.order.domain.OrderState;
import com.hali.spring.deliveryms.order.domain.Product;
import com.hali.spring.deliveryms.order.repositories.ItemRedisRepository;
import com.hali.spring.deliveryms.order.repositories.OrderRepository;
import com.hali.spring.deliveryms.order.services.OrderService;
import com.hali.spring.deliveryms.order.utils.CartUtilities;
import com.hali.spring.deliveryms.order.utils.JsonUtil;

import lombok.extern.slf4j.Slf4j;
import se.svt.oss.junit5.redis.EmbeddedRedisExtension;

@SpringBootTest(classes = {TestKAFKAConfig.class})
@Slf4j
@EmbeddedKafka(partitions = 1,bootstrapServersProperty = "${spring.kafka.bootstrap-servers}",
		topics = {MessagingBeanConfig.
		ORDER_VALIDATE_QUEUE_REQUEST,MessagingBeanConfig.ORDER_VALIDATE_QUEUE_RESPONSE})
public class OrderServiceIT 
{
	@RegisterExtension
    static EmbeddedRedisExtension staticExtension = new EmbeddedRedisExtension(true);
	
	@Autowired
	private OrderService service;

	@Autowired
	private EmbeddedKafkaBroker embeddedKafkaBroker;

	@Autowired
	ObjectMapper objMapper;
	
	@Autowired
	OrderRepository orderRepo;
	
	@Autowired
	KafkaTemplate<String,Object> template;
	
	@Autowired
	ItemRedisRepository itemRedisRepository;
	
	OrderDto order;
	
	String cartId;

	//	private KafkaMessageListenerContainer<String, Object> container;
	//
	//    private BlockingQueue<ConsumerRecord<String, String>> consumerRecords;

	@Test
	public void testAddOrder()
	{
		Order order = new  Order(); 
		order = new Order();
		order.setOrderedDate(LocalDate.now());
		
		Product p = new Product();
	
		p.setId("11");
		p.setPrice(new BigDecimal(100));
		p.setProductName("p1");	
		
		order.setItems(new ArrayList<>());
		
		order.getItems().add(new Item(1, p, new BigDecimal(100)));
		
		Order saved = orderRepo.saveAndFlush(order);
		
	
		Assertions.assertTrue(orderRepo.findById(saved.getId()).isPresent());
	}
	
	private void addProductsToCart() throws JsonProcessingException
	{
		ProductDto product = new ProductDto();
		product.setPrice(new BigDecimal(200));
		product.setProductName("PROD1");
		product.setId("1");
		ItemDto item = new ItemDto(2, product, CartUtilities.getSubTotalForItem(product,2));
		
		ProductDto product2 = new ProductDto();
		product2.setPrice(new BigDecimal(400));
		product2.setProductName("PROD2");
		product2.setId("2");
		ItemDto item2 = new ItemDto(4, product2, CartUtilities.getSubTotalForItem(product,4));
			
		itemRedisRepository.addItemToCart(cartId, product.getId().toString(), item);
		itemRedisRepository.addItemToCart(cartId, product2.getId().toString(), item2);
		
	}
	
	@BeforeEach
	void setUp() throws Exception
	{
		cartId  = "1234456";
//		 System.setProperty("spring.kafka.bootstrap-servers", embeddedKafkaBroker.getBrokersAsString());
		order = new OrderDto();
		order.setPickupAddress("Add_Pickup");
		order.setPickupAddressType(false);
		order.setPickupLocation(new Point(2, 2));
		
		order.setDeliveryAddress("Ad_Delivery");
		order.setDeliveryAddressType(true);
		order.setDeliveryLocation(new Point(1, 1));
		
		order.setReferenceNumber("R1234");

		orderRepo.deleteAll();
		itemRedisRepository.deleteCart(cartId);
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
	public void testValidationRequest() throws Exception
	{
		addProductsToCart();
		
		//    	 Map<String, Object> configs = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
		//         Producer<String, String> producer = new DefaultKafkaProducerFactory<>(configs, new StringSerializer(), new StringSerializer()).createProducer();
		//
		ValidateOrderReponse resp = ValidateOrderReponse.builder().orderId("1").validated(false).build();
		//         
		//         
		//         producer.send(new ProducerRecord<>(MessagingBeanConfig.
		//        		 ORDER_VALIDATE_QUEUE_REQUEST, "my-evt-id", objMapper.writeValueAsString(resp)));
		//         producer.flush();
		//
		//         
		//         ConsumerRecord<String, String> singleRecord = consumerRecords.poll(100, TimeUnit.MILLISECONDS);
		//         assertThat(singleRecord).isNotNull();

		
		
		Consumer<String, Bytes> consumer = TestKAFKAConfig.configureConsumer(embeddedKafkaBroker,
															MessagingBeanConfig.ORDER_VALIDATE_QUEUE_REQUEST);
//		Producer<Integer, String> producer = configureProducer();
//
//		producer.send(new ProducerRecord<>(MessagingBeanConfig.ORDER_VALIDATE_QUEUE_REQUEST, 
//				123,  objMapper.writeValueAsString(resp)));
		
		OrderDto saved = service.createOrder(cartId, order);

		ConsumerRecord<String, Bytes> singleRecord = KafkaTestUtils.getSingleRecord(consumer, 
				MessagingBeanConfig.ORDER_VALIDATE_QUEUE_REQUEST);
		
		System.out.println(singleRecord.value().toString());
		
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
		addProductsToCart();

		OrderDto saved = service.createOrder(cartId, order);
		
		ValidateOrderReponse resp = ValidateOrderReponse.builder().
				orderId(saved.getId()).validated(false).build();
		
		Consumer<String, Bytes> consumer = TestKAFKAConfig.configureConsumer(embeddedKafkaBroker,
				MessagingBeanConfig.ORDER_VALIDATE_QUEUE_REQUEST);
		
		ConsumerRecord<String, Bytes> singleRecord = KafkaTestUtils.getSingleRecord(consumer, 
				MessagingBeanConfig.ORDER_VALIDATE_QUEUE_REQUEST);
		
		assertThat(singleRecord).isNotNull();	
		consumer.close();

		
		template.send(MessagingBeanConfig.ORDER_VALIDATE_QUEUE_RESPONSE,resp);	
		
		Awaitility.await().atMost(Durations.FIVE_SECONDS).untilAsserted(()->{
			Order fetchOrder =  orderRepo.findById(saved.getId()).get();

			Assertions.assertEquals(OrderState.VALIDATION_FAILED, fetchOrder.getCurrentState());
		});	
	}
	
	@Test
	public void testValidationValidResponse() throws Exception
	{		
		addProductsToCart();
		
		
		OrderDto saved = service.createOrder(cartId, order);
//		Producer<Integer, Object> producer = configureProducer();

		ValidateOrderReponse resp = ValidateOrderReponse.builder()
					.orderId(saved.getId())
					.validated(true).build();
		
//		producer.send(new ProducerRecord<>(MessagingBeanConfig.ORDER_VALIDATE_QUEUE_RESPONSE, 
//				123,  resp));		
		Consumer<String, Bytes> consumer = TestKAFKAConfig.configureConsumer(embeddedKafkaBroker,
				MessagingBeanConfig.ORDER_VALIDATE_QUEUE_REQUEST);
		
		ConsumerRecord<String, Bytes> singleRecord = KafkaTestUtils.getSingleRecord(consumer, 
				MessagingBeanConfig.ORDER_VALIDATE_QUEUE_REQUEST);
		
		assertThat(singleRecord).isNotNull();
		consumer.close();
		
		template.send(MessagingBeanConfig.ORDER_VALIDATE_QUEUE_RESPONSE, resp);
		
		Awaitility.await().atMost(Durations.FIVE_SECONDS).untilAsserted(()->{
			Order fetchOrder =  orderRepo.findById(saved.getId()).get();

			Assertions.assertEquals(OrderState.PLACED, fetchOrder.getCurrentState());
		});
		
//		producer.close();	
	}
	
	@Test
	@Disabled
	public void testNewToReadyForDeliveryPrePaid() throws Exception
	{		
		addProductsToCart();
		
		order.setPrePaid(true);		
		
		OrderDto saved = service.createOrder(cartId, order);
//		Producer<Integer, Object> producer = configureProducer();
		
		ValidateOrderReponse resp = ValidateOrderReponse.builder().
				orderId(saved.getId()).validated(true).build();		
	
		
		Consumer<String, Bytes> consumerOrderValidate = TestKAFKAConfig.configureConsumer(embeddedKafkaBroker,
				MessagingBeanConfig.ORDER_VALIDATE_QUEUE_REQUEST);
		
		ConsumerRecord<String, Bytes> singleRecord = KafkaTestUtils.getSingleRecord(consumerOrderValidate, 
				MessagingBeanConfig.ORDER_VALIDATE_QUEUE_REQUEST);
		
		assertThat(singleRecord).isNotNull();
		consumerOrderValidate.close();
		
//		producer.send(new ProducerRecord<>(MessagingBeanConfig.ORDER_VALIDATE_QUEUE_RESPONSE, 
//				123,  resp));
		
		template.send(MessagingBeanConfig.ORDER_VALIDATE_QUEUE_RESPONSE, resp);
		
		Awaitility.await().atMost(Durations.FIVE_SECONDS).untilAsserted(()->{
			Order fetchOrder =  orderRepo.findById(saved.getId()).get();

			Assertions.assertEquals(OrderState.PLACED, fetchOrder.getCurrentState());
		});
		
		OrderPaymentResponse payRes = OrderPaymentResponse.builder().
				OrderId(saved.getId()).recevied(true).build();	
		
//		producer.send(new ProducerRecord<>(MessagingBeanConfig.ORDER_PAYMENT_QUEUE_RESPONSE, 
//				123,  payRes));		
		
//		template.send(MessagingBeanConfig.ORDER_PAYMENT_QUEUE_RESPONSE, payRes);
		template.send(MessagingBeanConfig.ORDER_PAYMENT_QUEUE_RESPONSE, payRes);
		
		Awaitility.await().atMost(Durations.FIVE_SECONDS).untilAsserted(()->{
			Order fetchOrder =  orderRepo.findById(saved.getId()).get();

			Assertions.assertEquals(OrderState.READY_FOR_DELIVERY, fetchOrder.getCurrentState());
		});
		
//		producer.close();	
	}

//	private Consumer<String, Bytes> configureConsumer(String topic) {
//		Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(MessagingBeanConfig.KAFKA_GROUP_ID, "true", embeddedKafkaBroker);
//		consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//		consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, MessagingBeanConfig.KAFKA_GROUP_ID);
//		consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, BytesDeserializer.class);
//		consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//		Consumer<String, Bytes> consumer = new DefaultKafkaConsumerFactory<String, Bytes>(consumerProps)
//				.createConsumer();
//		consumer.subscribe(Collections.singleton(topic));
//		return consumer;
//	}

//	private Producer<String,Object> configureProducer() {
//		Map<String, Object> producerProps = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
//		
//		producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//		producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//		return new DefaultKafkaProducerFactory<Integer, Object>(producerProps).createProducer();
//	}
}
