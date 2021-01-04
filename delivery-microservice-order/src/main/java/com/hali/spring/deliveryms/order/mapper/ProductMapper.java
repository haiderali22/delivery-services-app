package com.hali.spring.deliveryms.order.mapper;

import org.mapstruct.Mapper;

import com.hali.spring.deliveryms.model.ProductDto;
import com.hali.spring.deliveryms.order.domain.Product;

@Mapper
public interface ProductMapper 
{
	Product map(ProductDto product);
	ProductDto map(Product order);
}
