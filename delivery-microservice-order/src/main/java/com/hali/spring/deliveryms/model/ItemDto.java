package com.hali.spring.deliveryms.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemDto implements Serializable
{
	@EqualsAndHashCode.Include
	private Long id;
	private int quantity;	
	
	private BigDecimal subTotal;
	private ProductDto product;
	
	public ItemDto() {}

	public ItemDto(int quantity, ProductDto product, BigDecimal subTotal) {
		this.quantity = quantity;
		this.product = product;
		this.subTotal = subTotal;		
	}
}
