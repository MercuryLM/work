package com.example.feignconsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class FeginController {
    @Autowired
    private HelloFeignService computeclient;

    String serverUrl = "http://SERVER-PROVIDE";

    @GetMapping("/consumer")
    public String consumer(@RequestParam("word") String word) {

        return "输入为："+ word + computeclient.hello();

    }
}
