package com.example.hystrixfeign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
public class HystrixfeignApplication {

	public static void main(String[] args) {
		SpringApplication.run(HystrixfeignApplication.class, args);
	}
	@Bean
	@LoadBalanced
		//加入负载均衡能力
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
