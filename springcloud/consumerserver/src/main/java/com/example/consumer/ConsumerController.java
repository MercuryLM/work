package com.example.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ConsumerController {

    @Autowired
    RestTemplate restTemplate;

    String serverUrl = "http://SERVER-PROVIDE";

    @GetMapping("/consumer")
    public String consumer(@RequestParam("name") String name) {
        //word = "mercury";
        /*ResponseEntity<String> entity
                = restTemplate.getForEntity(serverUrl + "/hello?word={1}", String.class,word);
        return "调用服务返回值为：" + entity.getBody();*/
        return name + restTemplate.getForEntity(serverUrl + "/hello",String.class).getBody();
    }

}

