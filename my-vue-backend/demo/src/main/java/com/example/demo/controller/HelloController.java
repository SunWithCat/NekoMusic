package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController // 这是一个控制器
public class HelloController {
    @GetMapping("/api/hello")
    public String sayHello() {
        return "你好，我是从Spring Boot后端发来的消息！👋";
    }
}