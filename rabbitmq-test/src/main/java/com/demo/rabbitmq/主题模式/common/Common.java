package com.demo.rabbitmq.主题模式.common;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Common {
    private static final String RABBIT_MQ_HOST = "bailegedu.com";
    private static final Integer RABBIT_MQ_PORT = 5672;
    private static final String RABBIT_MQ_USERNAME = "admin";
    private static final String RABBIT_MQ_PASSWORD = "admin";

    public static Channel getChannel() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Common.RABBIT_MQ_HOST);
        factory.setPort(Common.RABBIT_MQ_PORT);
        factory.setUsername(Common.RABBIT_MQ_USERNAME);
        factory.setPassword(Common.RABBIT_MQ_PASSWORD);
        Connection connection = factory.newConnection();
        return connection.createChannel();
    }


}