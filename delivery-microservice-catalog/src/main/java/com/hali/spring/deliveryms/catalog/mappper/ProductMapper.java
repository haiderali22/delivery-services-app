package com.hali.spring.deliveryms.catalog.mappper;

import org.mapstruct.Mapper;

import com.hali.spring.deliveryms.catalog.domain.Product;
import com.hali.spring.deliveryms.catalog.model.ProductDto;


@Mapper
public interface ProductMapper {
	Product map(ProductDto product);
	ProductDto map(Product order);
}
