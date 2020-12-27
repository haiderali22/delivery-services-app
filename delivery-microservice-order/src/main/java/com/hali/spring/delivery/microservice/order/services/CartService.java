package com.hali.spring.delivery.microservice.order.services;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.hali.spring.delivery.microservice.order.repositories.ItemRedisRepository;
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
	private final ItemRedisRepository itemRedisRepository;

	public List<ItemDto> getCart(String cartId) 
	{		
		return itemRedisRepository.getCart(cartId);		
	}

	public void addItemToCart(String cartId, Long productId, Integer quantity) 
	{
		ProductDto product = productClient.getProductById(productId);
		ItemDto item = new ItemDto(quantity, product, CartUtilities.getSubTotalForItem(product,quantity));
		
		itemRedisRepository.addItemToCart(cartId, product.getId().toString(),item);
	}

	public void changeItemQuantity(String cartId, Long productId, Integer quantity) {
		List<ItemDto> cart = getCart(cartId);

		for(ItemDto item : cart){
			if((item.getProduct().getId()).equals(productId)){
				itemRedisRepository.deleteItemFromCart(cartId, productId.toString());            
				item.setQuantity(quantity);
				item.setSubTotal(CartUtilities.getSubTotalForItem(item.getProduct(),quantity));           
				itemRedisRepository.addItemToCart(cartId, productId.toString(),item);
			}
		}
	}

	public void deleteCart(String cartId) {
		itemRedisRepository.deleteCart(cartId);
	}

	public void removeItemFromCart(String cartId, Long productId) {
		//List<ItemDto> cart = getCart(cartId);

		itemRedisRepository.deleteItemFromCart(cartId, productId.toString());
		
//		for(ItemDto item : cart){
//			if((item.getProduct().getId()).equals(productId)){
//				redisTemplate.opsForHash().delete(cartId, item.getProduct().getId());      
//			}
//		}		
	}

	public boolean checkIfItemIsExist(String cartId, Long productId) {
		
		return itemRedisRepository.checkKeyExists(cartId, productId.toString());
		
//		List<ItemDto> cart = getCart(cartId);
//		for(ItemDto item : cart){
//			if((item.getProduct().getId()).equals(productId)){
//				return true;
//			}
//		}
//		return false;
	}


}
