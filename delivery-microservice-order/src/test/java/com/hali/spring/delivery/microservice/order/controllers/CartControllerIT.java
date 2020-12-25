package com.hali.spring.delivery.microservice.order.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.hali.spring.delivery.ms.model.ProductDto;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureWireMock
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
		product.setProductName("PROD1");
		
		
		
		stubFor(WireMock.get("/api/product/" + productId)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(product))
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
		mockMvc.perform(get("/api/order/cart/{cartID}", cartId ))
		.andExpect(status().isOk());		
	}

	@Test
	void testAddItemToCart()throws Exception {
		mockMvc.perform(post("/api/order/cart/{cartID}", cartId ).param("productId", productId).param("quantity", "2"))
		.andExpect(status().isOk());
	}

	@Test
	void testRemoveItemFromCart() throws Exception{
		mockMvc.perform(delete("/api/order/cart/{cartID}", cartId ).param("productId", productId))
		.andExpect(status().isOk());
	}

}
