package com.demo.rabbitmq.发布订阅模式.producer;

import com.demo.rabbitmq.发布订阅模式.common.CommonField;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(CommonField.RABBIT_MQ_HOST);
        factory.setPort(CommonField.RABBIT_MQ_PORT);
        factory.setUsername(CommonField.RABBIT_MQ_USERNAME);
        factory.setPassword(CommonField.RABBIT_MQ_PASSWORD);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //Actively declare a non-autodelete, non-durable exchange with no extra arguments
        //主动声明一个不自动删除、不持久化的五额外参数的交换机
        //logs：交换机名称
        //fanout：交换机类型
        channel.exchangeDeclare("logs", "fanout");

        for (int i = 0; ; i++) {
            String msg = "这是第" + i + "条消息";
            //发送消息
            channel.basicPublish("logs", "", null, msg.getBytes(StandardCharsets.UTF_8));
            System.out.println("第" + i + "条消息已发送");
            Thread.sleep(1000);
        }

    }
}
