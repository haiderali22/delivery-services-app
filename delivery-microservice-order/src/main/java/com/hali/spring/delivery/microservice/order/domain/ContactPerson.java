package com.hali.spring.delivery.microservice.order.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable()
@Setter
@Getter
public class ContactPerson 
{
	private String number;

	@Column(name = "name")
	private 	String name;
}
