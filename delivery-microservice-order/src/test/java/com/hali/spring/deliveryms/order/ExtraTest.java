package com.hali.spring.deliveryms.order;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.data.geo.Point;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hali.spring.deliveryms.model.OrderDto;

public class ExtraTest {



	@Test
	public void testJson() throws JsonProcessingException
	{
		GeometryFactory geometryFactory = new GeometryFactory();
		
		OrderDto order = new OrderDto();

		order.setPickupAddress("Add_Pickup");
		order.setPickupAddressType(false);
		//order.setPickupLocation(geometryFactory.createPoint(new Coordinate(1, 1)));

		order.setDeliveryAddress("Ad_Delivery");
		order.setDeliveryAddressType(true);
	//	order.setDeliveryLocation(geometryFactory.createPoint(new Coordinate(2, 2)));
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JtsModule());
		order.setReferenceNumber("R1234");
		System.out.println(mapper.writeValueAsString(order));

	}
}
