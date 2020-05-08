package com.hali.spring.delivery.microservice.order.config.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JPABeanConfig 
{
	@Bean
    public AuditorAware<Long> auditorAware() 
	{
        return new AuditorAwareImpl();
    }
}
