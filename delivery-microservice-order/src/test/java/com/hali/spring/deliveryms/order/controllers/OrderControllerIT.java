package com.hali.spring.deliveryms.order.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.utils.Bytes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hali.spring.deliveryms.model.ItemDto;
import com.hali.spring.deliveryms.model.OrderDto;
import com.hali.spring.deliveryms.model.ProductDto;
import com.hali.spring.deliveryms.order.TestKAFKAConfig;
import com.hali.spring.deliveryms.order.config.messaging.MessagingBeanConfig;
import com.hali.spring.deliveryms.order.repositories.ItemRedisRepository;
import com.hali.spring.deliveryms.order.utils.CartUtilities;

import se.svt.oss.junit5.redis.EmbeddedRedisExtension;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1,bootstrapServersProperty = "${spring.kafka.bootstrap-servers}",
topics = {MessagingBeanConfig.
		ORDER_VALIDATE_QUEUE_REQUEST,MessagingBeanConfig.ORDER_VALIDATE_QUEUE_RESPONSE})
class OrderControllerIT {

	@RegisterExtension
	static EmbeddedRedisExtension staticExtension = new EmbeddedRedisExtension(true);

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
		
		GeometryFactory geometryFactory = new GeometryFactory(); 

		order.setPickupAddress("Add_Pickup");
		order.setPickupAddressType(false);
		//order.setPickupLocation(geometryFactory.createPoint(new Coordinate(1, 1)));

		order.setDeliveryAddress("Ad_Delivery");
		order.setDeliveryAddressType(true);
	//	order.setDeliveryLocation(geometryFactory.createPoint(new Coordinate(2, 2)));

		order.setReferenceNumber("R1234");

		Consumer<String, Bytes> consumer = TestKAFKAConfig.configureConsumer(embeddedKafkaBroker,
				MessagingBeanConfig.ORDER_VALIDATE_QUEUE_REQUEST);

		mockMvc.perform(post("/api/order/{cartID}", cartId ).content(objectMapper.writeValueAsString(order)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		ConsumerRecord<String, Bytes> singleRecord = KafkaTestUtils.getSingleRecord(consumer, 
				MessagingBeanConfig.ORDER_VALIDATE_QUEUE_REQUEST);

		assertThat(singleRecord).isNotNull();
		System.out.println("Log Listener request " + singleRecord.value());
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

	

}
