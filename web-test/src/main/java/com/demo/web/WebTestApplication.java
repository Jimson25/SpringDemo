package com.demo.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.demo.**.mapper")
public class WebTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebTestApplication.class, args);
    }

}
