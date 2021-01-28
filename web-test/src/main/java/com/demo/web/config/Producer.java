package com.demo.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Queue;
import javax.jms.Topic;

@RestController
public class Producer {
    @Autowired
    private JmsMessagingTemplate jmsTemplate;

    @Autowired
    private Queue queue;

    @Autowired
    private Topic topic;

    @GetMapping("/queue")
    public void sendQueueMsg(String msg) {
        jmsTemplate.convertAndSend(queue, msg);
    }

    @GetMapping("/topic")
    public void sendTopicMsg(String msg) {
        jmsTemplate.convertAndSend(topic, msg);
    }


}
