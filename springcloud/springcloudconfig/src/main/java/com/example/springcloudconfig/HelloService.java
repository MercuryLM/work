package com.example.springcloudconfig;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface HelloService {
    @RequestMapping("/hello")
    String hello(@RequestParam("name") String name);
}
