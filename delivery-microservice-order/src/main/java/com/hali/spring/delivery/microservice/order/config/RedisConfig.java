package com.hali.spring.delivery.microservice.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.hali.spring.delivery.microservice.order.domain.Item;

@Configuration
public class RedisConfig 
{
	@Bean
	@Primary
	RedisTemplate<String, Item> redisTemplate(RedisConnectionFactory rcf) {

		RedisTemplate<String, Item> template = new RedisTemplate<>();
		template.setConnectionFactory(rcf);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

		return template;
	}
	
}
