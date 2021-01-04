package com.hali.spring.deliveryms.order.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.hali.spring.deliveryms.model.OrderDto;
import com.hali.spring.deliveryms.order.domain.Location;
import com.hali.spring.deliveryms.order.domain.Order;

@Mapper
public interface OrderMapper 
{
	@Mapping(target = "pickupLocation", ignore = true)
	@Mapping(target = "deliveryLocation", ignore = true)
	Order map(OrderDto order);

	@Mapping(target = "pickupLocation", ignore = true)
	@Mapping(target = "pickupAddress", ignore = true)
	@Mapping(target = "pickupAddressType", ignore = true)
	@Mapping(target = "deliveryLocation", ignore = true)
	@Mapping(target = "deliveryAddress", ignore = true)
	@Mapping(target = "deliveryAddressType", ignore = true)
	OrderDto map(Order order);

	@AfterMapping
	default void setBookAuthor(@MappingTarget OrderDto orderDto, Order order) {		
		Location pickupLocation  = order.getPickupLocation();

		if(pickupLocation != null){
			orderDto.setPickupAddress(pickupLocation.getAddress());
			orderDto.setPickupLocation(pickupLocation.getLocation());
			orderDto.setPickupAddressType(pickupLocation.isSystemGenerated());
		}

		Location deliveryLocation  = order.getDeliveryLocation();

		if(deliveryLocation != null){
			orderDto.setDeliveryAddress(deliveryLocation.getAddress());
			orderDto.setDeliveryLocation(deliveryLocation.getLocation());
			orderDto.setDeliveryAddressType(deliveryLocation.isSystemGenerated());
		}

	}

	@AfterMapping
	default void setBookAuthor(@MappingTarget Order order , OrderDto orderDto) {

		Location pickupLocation  = new Location();

		pickupLocation.setLocation(orderDto.getPickupLocation());
		pickupLocation.setAddress(orderDto.getPickupAddress());
		pickupLocation.setSystemGenerated(orderDto.isPickupAddressType());

		Location deliveryLocation  = new Location();

		deliveryLocation.setLocation(orderDto.getDeliveryLocation());
		deliveryLocation.setAddress(orderDto.getDeliveryAddress());
		deliveryLocation.setSystemGenerated(orderDto.isDeliveryAddressType());

		order.setDeliveryLocation(deliveryLocation);
		order.setPickupLocation(pickupLocation);

	}
}
