package com.hali.spring.deliveryms.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.hali.spring.deliveryms.model.ItemDto;
import com.hali.spring.deliveryms.order.domain.Item;

@Configuration
public class RedisConfig 
{
	@Bean
	@Primary
	RedisTemplate<String, ItemDto> redisTemplate(RedisConnectionFactory rcf) {

		RedisTemplate<String, ItemDto> template = new RedisTemplate<>();
		template.setConnectionFactory(rcf);
		template.setKeySerializer(new StringRedisSerializer());
				
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

		return template;
	}
	
}
