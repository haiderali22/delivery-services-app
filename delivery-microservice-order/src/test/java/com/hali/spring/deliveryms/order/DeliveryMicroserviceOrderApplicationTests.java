package com.hali.spring.deliveryms.order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import se.svt.oss.junit5.redis.EmbeddedRedisExtension;

@SpringBootTest
@EmbeddedKafka(partitions = 1,bootstrapServersProperty = "${spring.kafka.bootstrap-servers}")
class DeliveryMicroserviceOrderApplicationTests {
	
	@RegisterExtension
    static EmbeddedRedisExtension staticExtension = new EmbeddedRedisExtension(true);	
	
	@Test
	void contextLoads() {
	}

}
