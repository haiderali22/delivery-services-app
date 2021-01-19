package com.hali.spring.delivery;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadBalancedRouteConfig {
	
	@Bean
	public RouteLocator loadBalancedRoutes(RouteLocatorBuilder builder)
	{
		return builder.routes()
				.route("order-service", r-> r.path("/api/order/**").
						uri("lb://order-service"))
				.route("product-service", r-> r.path("/api/product/**").
						uri("lb://prodct-catalog-service"))
				.build();
	}
}
