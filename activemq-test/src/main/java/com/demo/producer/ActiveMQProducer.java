package com.demo.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Queue;
import javax.jms.Topic;

@RestController
@RequestMapping("/activemq")
public class ActiveMQProducer {
    @Autowired
    private Queue queue;
    @Autowired
    private Topic topic;
    @Autowired
    private JmsMessagingTemplate jmsTemplate;

    @GetMapping("/queue")
    public void sendQueue(String msg) {
        jmsTemplate.convertAndSend(queue, msg);
    }

    @GetMapping("/topic")
    public void sendTopic(String msg){
        jmsTemplate.convertAndSend(topic,msg);
    }


}
