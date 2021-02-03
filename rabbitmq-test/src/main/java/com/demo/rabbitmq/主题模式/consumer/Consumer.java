package com.demo.rabbitmq.主题模式.consumer;

import com.demo.rabbitmq.主题模式.common.Common;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = Common.getChannel();

        //声明一个队列并获取队列名
        String queue = channel.queueDeclare().getQueue();

        List<String> keys = new ArrayList<>();
        System.out.println("请输入routingKey，输入`Q`结束:");
        while (true) {
            String routingKey = new Scanner(System.in).nextLine();
            if ("Q".equals(routingKey)) {
                break;
            }
            keys.add(routingKey);
        }

        keys.forEach(key -> {
            try {
                channel.queueBind(queue, "phone_exchange", key);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("message: " + new String(message.getBody(), StandardCharsets.UTF_8) + " === RoutingKey: " + message.getEnvelope().getRoutingKey());
        };

        CancelCallback cancelCallback = new CancelCallback() {
            @Override
            public void handle(String consumerTag) throws IOException {
                System.out.println("consumerTag: " + consumerTag);
            }
        };

        channel.basicConsume(queue, true, deliverCallback, cancelCallback);

    }
}
