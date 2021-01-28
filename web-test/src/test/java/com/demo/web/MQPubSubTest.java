package com.demo.web;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.jupiter.api.Test;

import javax.jms.*;

public class MQPubSubTest {
    @Test
    public void test1() throws Exception {
        //获取连接工厂
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.1.172:61616");
        //获取连接
        Connection conn = factory.createConnection();
        //开启连接
        conn.start();
        //创建一个会话
        Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //创建一个topic（主题）
        Topic topic = session.createTopic("test-topic");
        //创建一个生产者并绑定这个topic
        MessageProducer producer = session.createProducer(topic);
        for (int i = 0; ; i++) {
            //topic中发送消息
            TextMessage message = session.createTextMessage("第 " + i + " 条消息");
            System.out.println("生产第 " + i + " 条消息");
            producer.send(message);
            Thread.sleep(1000);
        }
    }

    @Test
    public void test2() throws Exception {
        //获取连接工厂
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.1.172:61616");
        //获取连接
        Connection conn = factory.createConnection();
        //开启连接
        conn.start();
        //创建一个会话
        Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //创建一个topic
        Topic topic = session.createTopic("test-topic");
        //创建一个消费者并订阅这个主题
        MessageConsumer consumer = session.createConsumer(topic);
        while (true) {
            //接收消息
            consumer.setMessageListener(message -> {
                TextMessage textMessage = (TextMessage) message;
                String text;
                try {
                    text = textMessage.getText();
                    System.out.println("接收到消息==> "+text);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
