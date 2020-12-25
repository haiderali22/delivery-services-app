package com.hali.spring.delivery.microservice.order.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.hali.spring.delivery.microservice.order.config.statemachine.OrderStateChangeInterceptor;
import com.hali.spring.delivery.microservice.order.domain.Item;
import com.hali.spring.delivery.microservice.order.domain.Order;
import com.hali.spring.delivery.microservice.order.domain.OrderEvent;
import com.hali.spring.delivery.microservice.order.domain.OrderState;
import com.hali.spring.delivery.microservice.order.domain.User;
import com.hali.spring.delivery.microservice.order.mapper.ItemMapper;
import com.hali.spring.delivery.microservice.order.mapper.OrderMapper;
import com.hali.spring.delivery.microservice.order.repositories.OrderRepository;
import com.hali.spring.delivery.microservice.order.restclient.UserClient;
import com.hali.spring.delivery.ms.model.ItemDto;
import com.hali.spring.delivery.ms.model.OrderDto;
import com.hali.spring.delivery.ms.model.OrderException;
import com.hali.spring.delivery.ms.order.utils.OrderUtilities;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderService 
{
//	public static final String ORDER_ID_HEADER = "order_id";
//	public static final String ORDER_PREPAID_HEADER = "order_pre_paid";
//
//	private final OrderRepository orderRepository;
//////	private final StateMachineService<OrderState, OrderEvent> stateMachineService;
//////	private final StateMachinePersist<OrderState, OrderEvent, String> stateMachinePersist;
//////	
//////	private final StateMachineListener listener;
////	private StateMachine<OrderState, OrderEvent> currentStateMachine;
//	
//	private final StateMachineFactory<OrderState, OrderEvent> factory;
//	private final  OrderStateChangeInterceptor orderStateChangeInterceptor;
	
	private final OrderManager orderManager;
	private final OrderMapper orderMapper;
	private final ItemMapper itemMapper;
	private final CartService cartService;
	private final UserClient userClient;

	public OrderDto createOrder(String cartId) throws OrderException
	{
		List<ItemDto> cart = cartService.getCart(cartId);
		
		String userId = ""; //TODO: get id of current user  
		
	    User user = new User();  
	    user.setId(1L);
	    //userClient.getUserById(userId);   
	    
	    if(cart != null && user != null) {
	    	Order order = this.createOrder(cart, user);
	    	
	    	Order savedOrder = orderManager.placeOrder(order);
	    	return orderMapper.map(savedOrder);
	    }
	    else
	    {
	    	throw new OrderException("Error in creating order");
	    }
	}
	
	 private Order createOrder(List<ItemDto> cart, User user) {
	        Order order = new Order();
	        order.setItems(cart.stream().map(itemMapper::map).collect(Collectors.toList()));
	        order.setUser(user);
	        order.setTotal(OrderUtilities.countTotalPrice(cart));
	        order.setOrderedDate(LocalDate.now());
	        order.setCurrentState(OrderState.NEW);
	        return order;
	    }
	

	public void paymentReceived(Long id) throws Exception {
		
		orderManager.paymentReceived(id);
	}

	public void orderRiderAssigned(Long id) throws Exception {
		orderManager.orderRiderAssigned(id);
	}



}
