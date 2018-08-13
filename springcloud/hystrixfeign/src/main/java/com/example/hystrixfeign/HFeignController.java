package com.example.hystrixfeign;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

public class HFeignController {

    @Autowired
    RestTemplate restTemplate;

    @Value("${pro}")
    String pro;

    @Autowired
    HelloFeignService feignService;

    String serverUrl = "http://SERVER-PROVIDE";

    @GetMapping("/consumer")
    @HystrixCommand(fallbackMethod = "consumerFallback")
    public String consumer(String word) {
        ResponseEntity<String> entity
                = restTemplate.getForEntity(serverUrl + "/hello?word={1}", String.class,word);
        return "调用服务返回值为：" + entity.getBody();
    }

    public String consumerFallback(String word) {
        return "word:" + word + ",熔断了!";
    }

    @GetMapping("/feign")
    public String feign(String word) {
        return "Feign调用服务返回值为：" + feignService.hello(word);
    }

    @GetMapping("/config")
    public String config() {
        return "properties:pro=" + pro;
    }
}
