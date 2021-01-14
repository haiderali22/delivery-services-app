package com.hali.spring.deliveryms.catalog.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bedatadriven.jackson.datatype.jts.JtsModule;

@Configuration
public class ConfigBeans 
{
	@Bean
    public JtsModule jtsModule() {
        return new JtsModule();
    }       
}