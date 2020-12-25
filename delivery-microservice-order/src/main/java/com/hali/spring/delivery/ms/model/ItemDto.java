package com.hali.spring.delivery.ms.model;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ItemDto 
{
	private Long id;
	private int quantity;	
	private BigDecimal subTotal;	
	private ProductDto product;

	public ItemDto(int quantity, ProductDto product, BigDecimal subTotal) {
		this.quantity = quantity;
		this.product = product;
		this.subTotal = subTotal;
	}
}
