package com.demo.rabbitmq.简单模式_boot.producer;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

// TODO: 2021/1/30  
@RestController
public class Producer {
    @Autowired
    private AmqpTemplate amqpTemplate;
    
    public String sendMsg(){
        amqpTemplate.convertAndSend("haha");
        
        return "";
    }
    
}
