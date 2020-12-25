package com.hali.spring.delivery.microservice.order.services;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.hali.spring.delivery.microservice.order.restclient.ProductClient;
import com.hali.spring.delivery.ms.model.ItemDto;
import com.hali.spring.delivery.ms.model.ProductDto;
import com.hali.spring.delivery.ms.order.utils.CartUtilities;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

	private final RedisTemplate<String, ItemDto> redisTemplate;
	private final ProductClient productClient;

	public List<ItemDto>  getCart(String cartId) 
	{
		Long size = redisTemplate.opsForList().size(cartId);

		return redisTemplate.opsForList().range(cartId,0, size);
	}

	public void addItemToCart(String cartId, Long productId, Integer quantity) 
	{
		ProductDto product = productClient.getProductById(productId);
		ItemDto item = new ItemDto(quantity, product, CartUtilities.getSubTotalForItem(product,quantity));
		
		redisTemplate.opsForList().rightPush(cartId, item);
	}

	public void changeItemQuantity(String cartId, Long productId, Integer quantity) {
		List<ItemDto> cart = getCart(cartId);

		for(ItemDto item : cart){
			if((item.getProduct().getId()).equals(productId)){
				redisTemplate.opsForList().remove(cartId, 0, item);                
				item.setQuantity(quantity);
				item.setSubTotal(CartUtilities.getSubTotalForItem(item.getProduct(),quantity));           
				redisTemplate.opsForList().rightPush(cartId, item);
			}
		}
	}

	public void deleteCart(String cartId) {
		redisTemplate.delete(cartId);
	}

	public void removeItemFromCart(String cartId, Long productId) {
		List<ItemDto> cart = getCart(cartId);

		for(ItemDto item : cart){
			if((item.getProduct().getId()).equals(productId)){
				redisTemplate.opsForList().remove(cartId, 0, item);	               
			}
		}		
	}

	public boolean checkIfItemIsExist(String cartId, Long productId) {
		List<ItemDto> cart = getCart(cartId);
		for(ItemDto item : cart){
			if((item.getProduct().getId()).equals(productId)){
				return true;
			}
		}
		return false;
	}


}
