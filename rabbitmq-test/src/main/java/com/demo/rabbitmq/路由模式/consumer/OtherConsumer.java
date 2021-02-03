package com.demo.rabbitmq.路由模式.consumer;

import com.demo.rabbitmq.路由模式.common.CommonField;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class OtherConsumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = CommonField.getChannel();
        //声明一个队列并获取队列名
        String queue = channel.queueDeclare().getQueue();
        //将队列绑定到交换机并根据这个队列业务类型接收多个级别的日志
        channel.queueBind(queue, "logs_direct", "info");
        channel.queueBind(queue, "logs_direct", "warning");

        DeliverCallback callback = (consumerTag, message) -> {
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);
            String routingKey = message.getEnvelope().getRoutingKey();
            System.out.println("收到: " + routingKey + " 消息: " + msg);
        };
        CancelCallback cancelCallback = consumerTag -> System.out.println("consumerTag: " + consumerTag);
        channel.basicConsume(queue, true, callback, cancelCallback);
    }
}
