package com.example.hystrixfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value="SERVER-PROVIDE",fallback=HelloFeignServiceFallback.class)

public interface HelloFeignService {
    @RequestMapping("/hello")
    public String hello(@RequestParam("word") String word);

}
