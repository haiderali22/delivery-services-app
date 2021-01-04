package com.hali.spring.delivery.microservice.order.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.geo.Point;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hali.spring.delivery.microservice.order.TestRedisConfiguration;
import com.hali.spring.deliveryms.model.ItemDto;
import com.hali.spring.deliveryms.model.OrderDto;
import com.hali.spring.deliveryms.model.ProductDto;
import com.hali.spring.deliveryms.order.config.messaging.MessagingBeanConfig;
import com.hali.spring.deliveryms.order.repositories.ItemRedisRepository;
import com.hali.spring.deliveryms.order.utils.CartUtilities;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,classes = TestRedisConfiguration.class)
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1,bootstrapServersProperty = "${spring.kafka.bootstrap-servers}",
	topics = {MessagingBeanConfig.
	ORDER_VALIDATE_QUEUE_REQUEST,MessagingBeanConfig.ORDER_VALIDATE_QUEUE_RESPONSE})
class OrderControllerIT {

	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	ItemRedisRepository itemRedisRepository;
	
	@Autowired
	private EmbeddedKafkaBroker embeddedKafkaBroker;
	
	String cartId;
	
	String productId = "1";
	
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
	void setUp() throws Exception {  
		
		cartId = "123456";
		addProductsToCart();
	}

	@Test
	void testSaveOrderSuccess() throws Exception {
		
		OrderDto order = new OrderDto();
		
		order.setPickupAddress("Add_Pickup");
		order.setPickupAddressType(false);
		order.setPickupLocation(new Point(2, 2));
		
		order.setDeliveryAddress("Ad_Delivery");
		order.setDeliveryAddressType(true);
		order.setDeliveryLocation(new Point(1, 1));
		
		order.setReferenceNumber("R1234");
		
		mockMvc.perform(post("/api/order/{cartID}", cartId ).content(objectMapper.writeValueAsString(order)).contentType(MediaType.APPLICATION_JSON)
			      	.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	
		Consumer<String, String> consumer = configureConsumer(MessagingBeanConfig.ORDER_VALIDATE_QUEUE_REQUEST);

		ConsumerRecord<String, String> singleRecord = KafkaTestUtils.getSingleRecord(consumer, 
				MessagingBeanConfig.ORDER_VALIDATE_QUEUE_REQUEST);
			
		assertThat(singleRecord).isNotNull();
		System.out.println("Log Listener request " + singleRecord.value());
	}
	
	private Consumer<String, String> configureConsumer(String topic) {
		Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("order-delivery", "true", embeddedKafkaBroker);
		consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		Consumer<String, String> consumer = new DefaultKafkaConsumerFactory<String, String>(consumerProps)
				.createConsumer();
		consumer.subscribe(Collections.singleton(topic));
		return consumer;
	}
}
