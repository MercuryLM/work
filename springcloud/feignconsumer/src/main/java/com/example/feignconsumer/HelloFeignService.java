package com.example.feignconsumer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("SERVER-PROVIDE")
public interface HelloFeignService {
    @RequestMapping("/hello")
    String hello();
}
