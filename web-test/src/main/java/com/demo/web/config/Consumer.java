package com.demo.web.config;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
    //接收queue类型的消息
    //destination对应配置类中配置的queue bean的名字
    @JmsListener(destination = "springboot.queue")
    public void ListenQueue(String msg) {
        System.out.println("接收到queue的消息===>: " + msg);
    }

    //接收topic类型的消息
    //destination对应配置类中配置的topic bean的名字
    @JmsListener(destination = "springboot.topic", containerFactory = "jmsListenerContainerFactory")
    public void ListenTopic(String msg) {
        System.out.println("接收到topic的消息===>: " + msg);
    }

}
