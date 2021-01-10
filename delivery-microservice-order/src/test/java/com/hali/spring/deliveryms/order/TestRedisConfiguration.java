//package com.hali.spring.deliveryms.order;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//
//import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
//import org.springframework.boot.test.context.TestConfiguration;
//
//import redis.embedded.RedisServer;
//
//@TestConfiguration
//public class TestRedisConfiguration {
//
//    private RedisServer redisServer;
//
//    public TestRedisConfiguration(RedisProperties redisProperties) {
//        this.redisServer = new RedisServer(redisProperties.getPort());
//    }
//
//    @PostConstruct
//    public void postConstruct() {
//    	if(!redisServer.isActive())
//    		redisServer.start();
//    }
//
//    @PreDestroy
//    public void preDestroy() {
//    	
//    	if(redisServer.isActive())
//        	redisServer.stop();
//    }
//}