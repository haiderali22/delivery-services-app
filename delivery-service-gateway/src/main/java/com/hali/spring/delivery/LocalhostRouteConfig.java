package com.hali.spring.delivery;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LocalhostRouteConfig {
	
	@Bean
	public RouteLocator localhostRoutes(RouteLocatorBuilder builder)
	{
		return builder.routes()
				.route("order-service", r-> r.path("/api/v1/order/**").
						uri("https://localhost:8081"))
				.route("product-service", r-> r.path("/api/v1/product/**").
						uri("https://localhost:8082"))
				.build();
	}
}
