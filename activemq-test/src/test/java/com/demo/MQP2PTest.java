package com.demo;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.jupiter.api.Test;

import javax.jms.*;

public class MQP2PTest {
    private static final String ACTIVEMQ_URL = "tcp://192.168.1.172:61616";

    @Test
    public void test1() throws JMSException, InterruptedException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);


        Destination testMQ = session.createQueue("testMQ");
        MessageProducer producer = session.createProducer(testMQ);

        for (int i = 0; ; i++) {
            TextMessage message = session.createTextMessage("第 " + i + " 条消息");
            producer.send(message);
            System.out.println("已发送第 " + i + " 条消息");
            Thread.sleep(1000);
        }

    }

    @Test
    public void test2() throws Exception {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination testMQ = session.createQueue("testMQ");

        MessageConsumer consumer = session.createConsumer(testMQ);

        while (true) {
            TextMessage receive = (TextMessage) consumer.receive();
            System.out.println(receive.getText());
        }
    }

    @Test
    public void test3() throws Exception {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination testMQ = session.createQueue("testMQ");

        MessageConsumer consumer = session.createConsumer(testMQ);

        while (true) {
            TextMessage receive = (TextMessage) consumer.receive();
            System.out.println(receive.getText());
        }
    }

}
