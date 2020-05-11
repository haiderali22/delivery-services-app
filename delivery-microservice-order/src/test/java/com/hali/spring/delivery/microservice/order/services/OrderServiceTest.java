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

@SpringBootTest
class OrderServiceTest 
{

	@Autowired
	OrderRepository orderRepo;

	@Autowired
	OrderHistoryRepository orderHRepo;

	@Autowired
	OrderService service;

	Order order;

	@BeforeEach
	void setUp() throws Exception 
	{

		order = new Order();
		order.setCurrentState(OrderState.NEW);

	}

	@Test
	@Transactional
	void testNewOrder() throws Exception {
		Order saved = service.newOrder(order); // postpaid

		Order fetchOrder =  orderRepo.getOne(saved.getId());

		Assertions.assertEquals(OrderState.READY_FOR_DELIVERY, fetchOrder.getCurrentState());
		
		
	}

	@Test
	@Transactional
	void testReceivePayment() throws Exception {
		order.setPrePaid(true);
		Order saved = service.newOrder(order);

		service.paymentReceived(saved.getId());

		Order fetchOrder =  orderRepo.getOne(saved.getId());

		Assertions.assertEquals(OrderState.READY_FOR_DELIVERY, fetchOrder.getCurrentState());
		
	}

	@Test
	@Transactional
	void testPostPaid() throws Exception {
		Order saved = service.newOrder(order);

		
		Order fetchOrder =  orderRepo.getOne(saved.getId());


		Assertions.assertEquals(OrderState.READY_FOR_DELIVERY, fetchOrder.getCurrentState());
	}

	@Test
	@Transactional
	void testPrePaid() throws Exception {
		order.setPrePaid(true);
		Order saved = service.newOrder(order);

		Order fetchOrder =  orderRepo.getOne(saved.getId());

		Assertions.assertEquals(OrderState.PLACED, fetchOrder.getCurrentState());
	}

	@Test
	@Transactional
	void testPickup() throws Exception {
		Order saved = service.newOrder(order);

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
