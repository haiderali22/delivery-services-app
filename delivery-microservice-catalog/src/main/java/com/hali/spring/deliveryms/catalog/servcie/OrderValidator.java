package com.hali.spring.deliveryms.catalog.servcie;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import com.hali.spring.deliveryms.catalog.model.OrderDto;
import com.hali.spring.deliveryms.catalog.repositories.ProductRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderValidator 
{
	private final ProductRepository productRepository;
	
	public boolean validateProduct(OrderDto order)
	{
		   AtomicInteger beersNotFound = new AtomicInteger();

		   order.getItems().forEach(item -> {
	            if( productRepository.existsById(item.getId()) ){
	                beersNotFound.incrementAndGet();
	            }
	        });

	        return beersNotFound.get() == 0;    		
		
	}
}
