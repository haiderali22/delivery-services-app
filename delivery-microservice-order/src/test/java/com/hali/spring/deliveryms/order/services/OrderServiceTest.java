package com.hali.spring.deliveryms.order.services;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.hali.spring.deliveryms.model.OrderDto;
import com.hali.spring.deliveryms.order.domain.Order;
import com.hali.spring.deliveryms.order.domain.OrderHistory;
import com.hali.spring.deliveryms.order.domain.OrderState;
import com.hali.spring.deliveryms.order.repositories.OrderHistoryRepository;
import com.hali.spring.deliveryms.order.repositories.OrderRepository;
import com.hali.spring.deliveryms.order.services.OrderService;

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
		OrderDto saved = service.createOrder(cartId, order); // postpaid

		Order fetchOrder =  orderRepo.getOne(saved.getId());

		Assertions.assertEquals(OrderState.READY_FOR_DELIVERY, fetchOrder.getCurrentState());
		
		
	}

	@Test
	@Transactional
	void testReceivePayment() throws Exception {
		order.setPrePaid(true);
		OrderDto saved = service.createOrder(cartId, order);

		service.paymentReceived(saved.getId());

		Order fetchOrder =  orderRepo.getOne(saved.getId());

		Assertions.assertEquals(OrderState.READY_FOR_DELIVERY, fetchOrder.getCurrentState());
		
	}

	@Test
	@Transactional
	void testPostPaid() throws Exception {
		OrderDto saved = service.createOrder(cartId, order);

		
		Order fetchOrder =  orderRepo.getOne(saved.getId());


		Assertions.assertEquals(OrderState.READY_FOR_DELIVERY, fetchOrder.getCurrentState());
	}

	@Test
	@Transactional
	void testPrePaid() throws Exception {
		order.setPrePaid(true);
		OrderDto saved = service.createOrder(cartId, order);

		Order fetchOrder =  orderRepo.getOne(saved.getId());

		Assertions.assertEquals(OrderState.PLACED, fetchOrder.getCurrentState());
	}

	@Test
	@Transactional
	void testPickup() throws Exception {
		OrderDto saved = service.createOrder(cartId, order);

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
