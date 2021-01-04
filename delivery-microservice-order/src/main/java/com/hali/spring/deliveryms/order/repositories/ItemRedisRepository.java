package com.hali.spring.deliveryms.order.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.hali.spring.deliveryms.model.ItemDto;

@Repository
public class ItemRedisRepository 
{
	private HashOperations<String, String, ItemDto> hashOperations;

	@Autowired
	public ItemRedisRepository(RedisTemplate<String, ItemDto> redisTemplate) {
		this.hashOperations = redisTemplate.opsForHash();
	}

	public void addItemToCart(String cartId, String key, ItemDto item) {
		hashOperations.put(cartId, key, item);
	}

	public List<ItemDto> getCart(String cartId){
		return hashOperations.values(cartId);
	}
	
	 public void deleteItemFromCart(String cartId, String key) { 
		 hashOperations.delete(cartId,key);
	 }
	
	 public void deleteCart(String cartId) { 
		 hashOperations.getOperations().delete(cartId);
	 }
	
	 public Boolean checkKeyExists(String cartId, String key) { 
		 return hashOperations.hasKey(cartId, key);
	 }
}
