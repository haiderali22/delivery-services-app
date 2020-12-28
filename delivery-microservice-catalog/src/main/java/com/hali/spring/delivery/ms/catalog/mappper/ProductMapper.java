package com.hali.spring.delivery.ms.catalog.mappper;

import org.mapstruct.Mapper;

import com.hali.spring.delivery.ms.catalog.domain.Product;
import com.hali.spring.delivery.ms.model.ProductDto;


@Mapper
public interface ProductMapper {
	Product map(ProductDto product);
	ProductDto map(Product order);
}
