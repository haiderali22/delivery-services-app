package com.hali.spring.delivery.microservice.order.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.hali.spring.delivery.microservice.order.TestRedisConfiguration;
import com.hali.spring.delivery.ms.model.ItemDto;
import com.hali.spring.delivery.ms.model.ProductDto;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,classes = TestRedisConfiguration.class)
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
class CartControllerIT {

	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	String cartId;
	
	String productId = "1";
	
	private void setUpAPi() throws JsonProcessingException
	{
		ProductDto product = new ProductDto();
		product.setPrice(new BigDecimal(200));
		product.setProductName("PROD1");
		product.setId(1L);
		
		ProductDto product2 = new ProductDto();
		product2.setPrice(new BigDecimal(400));
		product2.setProductName("PROD2");
		product2.setId(2L);
		
		
		
		stubFor(WireMock.get("/api/product/" + productId)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(product))
                )
        );
		
		stubFor(WireMock.get("/api/product/" + 2)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(product2))
                )
        );
	}
	
	@BeforeEach
	void setUp() throws Exception {  
		
		cartId = "123456";
		setUpAPi();
	}

	@Test
	void testGetCart() throws Exception {
		
		mockMvc.perform(post("/api/order/cart/{cartID}", cartId ).param("productId", "1").param("quantity", "2"))
		.andExpect(status().isOk());
		
		mockMvc.perform(post("/api/order/cart/{cartID}", cartId ).param("productId", "2").param("quantity", "4"))
		.andExpect(status().isOk());
		
		MvcResult result =  mockMvc.perform(get("/api/order/cart/{cartID}", cartId ))		
		.andExpect(status().isOk()).andReturn();
		
		ObjectMapper mapper = new ObjectMapper();
		
		List<ItemDto> actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<ItemDto>>() {});
		
		Assertions.assertEquals(2,actual.size());
		
		mockMvc.perform(delete("/api/order/cart/{cartID}", cartId ).param("productId", "1"))
		.andExpect(status().isOk());
		
		 result =  mockMvc.perform(get("/api/order/cart/{cartID}", cartId ))		
					.andExpect(status().isOk()).andReturn();
		
		actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<ItemDto>>() {});
		
		Assertions.assertEquals(1,actual.size());
	}

//	@Test
//	void testAddItemToCart()throws Exception {
//		mockMvc.perform(post("/api/order/cart/{cartID}", cartId ).param("productId", productId).param("quantity", "2"))
//		.andExpect(status().isOk());
//	}
//
//	@Test
//	void testRemoveItemFromCart() throws Exception{
//		mockMvc.perform(delete("/api/order/cart/{cartID}", cartId ).param("productId", productId))
//		.andExpect(status().isOk());
//	}

}
