package com.hali.spring.delivery.ms.model;

public class OrderException extends Exception 
{	
	private static final long serialVersionUID = 1L;
	
	public OrderException(String exp)
	{
		super(exp);
	}

}
