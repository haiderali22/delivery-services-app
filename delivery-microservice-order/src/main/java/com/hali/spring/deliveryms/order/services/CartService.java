package com.hali.spring.deliveryms.order.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hali.spring.deliveryms.model.ItemDto;
import com.hali.spring.deliveryms.model.ProductDto;
import com.hali.spring.deliveryms.order.repositories.ItemRedisRepository;
import com.hali.spring.deliveryms.order.restclient.ProductClient;
import com.hali.spring.deliveryms.order.utils.CartUtilities;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
	
	private final ProductClient productClient;
	private final ItemRedisRepository itemRedisRepository;

	public List<ItemDto> getCart(String cartId) 		
	{		
		return itemRedisRepository.getCart(cartId);		
	}

	public void addItemToCart(String cartId, String productId, Integer quantity) 
	{
		ProductDto product = productClient.getProductById(productId);
		ItemDto item = new ItemDto(quantity, product, CartUtilities.getSubTotalForItem(product,quantity));
		
		itemRedisRepository.addItemToCart(cartId, product.getId().toString(),item);
	}

	public void changeItemQuantity(String cartId, String productId, Integer quantity) {
		List<ItemDto> cart = getCart(cartId);

		for(ItemDto item : cart){
			if((item.getProduct().getId()).equals(productId)){
				itemRedisRepository.deleteItemFromCart(cartId, productId.toString());            
				item.setQuantity(quantity);
				item.setSubTotal(CartUtilities.getSubTotalForItem(item.getProduct(),quantity));           
				itemRedisRepository.addItemToCart(cartId, productId,item);
			}
		}
	}

	public void deleteCart(String cartId) {
		itemRedisRepository.deleteCart(cartId);
	}

	public void removeItemFromCart(String cartId, String productId) {
		//List<ItemDto> cart = getCart(cartId);

		itemRedisRepository.deleteItemFromCart(cartId, productId);
		
//		for(ItemDto item : cart){
//			if((item.getProduct().getId()).equals(productId)){
//				redisTemplate.opsForHash().delete(cartId, item.getProduct().getId());      
//			}
//		}		
	}

	public boolean checkIfItemIsExist(String cartId, String productId) {
		
		return itemRedisRepository.checkKeyExists(cartId, productId);
		
//		List<ItemDto> cart = getCart(cartId);
//		for(ItemDto item : cart){
//			if((item.getProduct().getId()).equals(productId)){
//				return true;
//			}
//		}
//		return false;
	}


}
