package com.hali.spring.delivery.ms.order.utils;

import java.math.BigDecimal;

import com.hali.spring.delivery.microservice.order.domain.Product;

public class CartUtilities {
	public static BigDecimal getSubTotalForItem(Product product, int quantity){
	       return (product.getPrice()).multiply(BigDecimal.valueOf(quantity));
	    }
}
