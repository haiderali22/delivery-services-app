package com.hali.spring.deliveryms.model;

public class OrderException extends RuntimeException 
{	
	private static final long serialVersionUID = 1L;
	
	public OrderException(String exp)
	{
		super(exp);
	}

}
