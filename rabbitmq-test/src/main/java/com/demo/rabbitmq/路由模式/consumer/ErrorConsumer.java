package com.demo.rabbitmq.路由模式.consumer;

import com.demo.rabbitmq.路由模式.common.CommonField;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class ErrorConsumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = CommonField.getChannel();
        //声明一个随机队列并获取队列名
        String queue = channel.queueDeclare().getQueue();
        //将队列绑定到交换机上并设置routingKey为error
        channel.queueBind(queue, "logs_direct", "error");
        //接收消息
        DeliverCallback callback = (consumerTag, message) -> {
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);
            String routingKey = message.getEnvelope().getRoutingKey();
            System.out.println("收到: " + routingKey + " 消息: " + msg);
        };

        CancelCallback cancelCallback = consumerTag -> System.out.println("consumerTag: " + consumerTag);
        // 根据上面的信息创建一个消费者
        channel.basicConsume(queue, true, callback, cancelCallback);
    }
}
