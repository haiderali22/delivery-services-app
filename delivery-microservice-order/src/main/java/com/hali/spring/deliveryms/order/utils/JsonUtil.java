package com.hali.spring.deliveryms.order.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil 
{
	public static String convertToString(ObjectMapper mapper, Object obj)
	{
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			return null;
		}
	}
}
