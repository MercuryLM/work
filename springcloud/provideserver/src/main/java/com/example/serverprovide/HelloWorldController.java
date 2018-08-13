package com.example.serverprovide;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.bouncycastle.crypto.tls.ConnectionEnd.server;

@RestController

public class HelloWorldController {


    /*public String helloWord(@RequestParam("word") String word) {
        return "请求参数为：" + word;
    }*/
    @Value("${server.port}")
            String port;

    @GetMapping("/hello")
    public String helloWord() {

        return "端口号为：" + port;
    }
}

