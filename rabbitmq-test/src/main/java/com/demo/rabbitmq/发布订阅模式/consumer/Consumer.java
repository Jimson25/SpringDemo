package com.demo.rabbitmq.发布订阅模式.consumer;

import com.demo.rabbitmq.发布订阅模式.common.CommonField;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
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
        //channel.exchangeDeclare("logs", "fanout");

        //Actively declare a server-named exclusive, autodelete, non-durable queue.
        //主动声明一个独占的、自动删除的、不持久化的队列，获取队列名称
        String queue = channel.queueDeclare().getQueue();

        //把该队列绑定到logs交换机
        //对于fanout交换机，routingkey会被忽略，不允许null值
        channel.queueBind(queue, "logs", "");
        DeliverCallback callback = (consumerTag, message) -> {
            System.out.println("consumerTag: " + consumerTag + " == 收到消息： " + (new String(message.getBody(), StandardCharsets.UTF_8)));
        };
        //消费者取消时的回调对象
        CancelCallback cancel = consumerTag -> {
        };

        channel.basicConsume(queue, true, callback, cancel);

    }


}
