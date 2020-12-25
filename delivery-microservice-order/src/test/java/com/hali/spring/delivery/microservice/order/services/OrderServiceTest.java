package com.hali.spring.delivery.microservice.order.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.transaction.annotation.Transactional;

import com.hali.spring.delivery.microservice.order.domain.Order;
import com.hali.spring.delivery.microservice.order.domain.OrderEvent;
import com.hali.spring.delivery.microservice.order.domain.OrderHistory;
import com.hali.spring.delivery.microservice.order.domain.OrderState;
import com.hali.spring.delivery.microservice.order.repositories.OrderHistoryRepository;
import com.hali.spring.delivery.microservice.order.repositories.OrderRepository;
import com.hali.spring.delivery.ms.model.OrderDto;

@SpringBootTest
class OrderServiceTest 
{

	@Autowired
	OrderRepository orderRepo;

	@Autowired
	OrderHistoryRepository orderHRepo;

	@Autowired
	OrderService service;

	OrderDto order;
	
	String cartId;

	@BeforeEach
	void setUp() throws Exception 
	{
		cartId = "123456";
		order = new OrderDto();
//		order.setCurrentState(OrderState.NEW);

	}

	@Test
	@Transactional
	void testNewOrder() throws Exception {
		OrderDto saved = service.createOrder(cartId); // postpaid

		Order fetchOrder =  orderRepo.getOne(saved.getId());

		Assertions.assertEquals(OrderState.READY_FOR_DELIVERY, fetchOrder.getCurrentState());
		
		
	}

	@Test
	@Transactional
	void testReceivePayment() throws Exception {
		order.setPrePaid(true);
		OrderDto saved = service.createOrder(cartId);

		service.paymentReceived(saved.getId());

		Order fetchOrder =  orderRepo.getOne(saved.getId());

		Assertions.assertEquals(OrderState.READY_FOR_DELIVERY, fetchOrder.getCurrentState());
		
	}

	@Test
	@Transactional
	void testPostPaid() throws Exception {
		OrderDto saved = service.createOrder(cartId);

		
		Order fetchOrder =  orderRepo.getOne(saved.getId());


		Assertions.assertEquals(OrderState.READY_FOR_DELIVERY, fetchOrder.getCurrentState());
	}

	@Test
	@Transactional
	void testPrePaid() throws Exception {
		order.setPrePaid(true);
		OrderDto saved = service.createOrder(cartId);

		Order fetchOrder =  orderRepo.getOne(saved.getId());

		Assertions.assertEquals(OrderState.PLACED, fetchOrder.getCurrentState());
	}

	@Test
	@Transactional
	void testPickup() throws Exception {
		OrderDto saved = service.createOrder(cartId);

//		service.paymentReceived(saved.getId());

		service.orderRiderAssigned(saved.getId());

		Order fetchOrder =  orderRepo.getOne(saved.getId());


		Assertions.assertEquals(OrderState.SENT_FOR_DELIVERY, fetchOrder.getCurrentState());


		List<OrderHistory> lst = orderHRepo.getAllByOrder(saved.getId());

		lst.forEach( o -> {
			System.out.println( o.getModifiedBy() + " " + o.getPreviousState() + " " +
					o.getEvent() + " " + o.getCurrentState());				
		});

	}

}
