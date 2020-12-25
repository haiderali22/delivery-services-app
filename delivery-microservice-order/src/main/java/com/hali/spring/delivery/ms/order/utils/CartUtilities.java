package com.hali.spring.delivery.ms.order.utils;

import java.math.BigDecimal;

import com.hali.spring.delivery.ms.model.ProductDto;

public class CartUtilities {
	public static BigDecimal getSubTotalForItem(ProductDto product, int quantity){
	       return (product.getPrice()).multiply(BigDecimal.valueOf(quantity));
	    }
}
