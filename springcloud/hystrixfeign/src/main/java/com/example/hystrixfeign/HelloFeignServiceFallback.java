package com.example.hystrixfeign;

public class HelloFeignServiceFallback implements HelloFeignService{
    @Override
    public String hello(String word) {
        return "Feign方式：word:" + word + ",熔断了!";
    }
}
