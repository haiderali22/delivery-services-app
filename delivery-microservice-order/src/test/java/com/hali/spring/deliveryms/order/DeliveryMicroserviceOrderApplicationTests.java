package com.hali.spring.deliveryms.order;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

@SpringBootTest
@EmbeddedKafka(partitions = 1,bootstrapServersProperty = "${spring.kafka.bootstrap-servers}")
class DeliveryMicroserviceOrderApplicationTests {

	@Test
	void contextLoads() {
	}

}
