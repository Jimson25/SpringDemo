package com.demo.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class ActiveMQConsumer {
    @JmsListener(destination = "springboot.queue")
    public void queueListen(String msg) {
        System.out.println("接收到queue消息:===> " + msg);
    }

    @JmsListener(destination = "springboot.topic", containerFactory = "jmsListenerContainerFactory")
    public void topicListen(String msg) {
        System.out.println("接收到topic消息:===> " + msg);
    }

}
