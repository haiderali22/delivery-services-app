package com.hali.spring.deliveryms.order.utils;

import java.math.BigDecimal;

import com.hali.spring.deliveryms.model.ProductDto;

public class CartUtilities {
	public static BigDecimal getSubTotalForItem(ProductDto product, int quantity){
	       return (product.getPrice()).multiply(BigDecimal.valueOf(quantity));
	    }
}
