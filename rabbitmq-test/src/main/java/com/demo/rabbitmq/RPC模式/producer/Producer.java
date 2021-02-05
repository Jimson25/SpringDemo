package com.demo.rabbitmq.RPC模式.producer;

import com.demo.rabbitmq.RPC模式.common.Common;
import com.rabbitmq.client.*;
import com.rabbitmq.client.AMQP.BasicProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Producer {
    public static void main(String[] args) throws IOException {
        Channel channel = Common.getChannel();

        //创建一个队列
        channel.queueDeclare("rpc_queue", false, false, false, null);
        //清除队列中的内容
        channel.queuePurge("rpc_queue");
        //一次只接收一条消息
        channel.basicQos(1);


        DeliverCallback deliverCallback = (consumerTag, message) -> {
            //处理收到的数据(要求第几个斐波那契数)
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);
            int n = Integer.parseInt(msg);
            //求出第n个斐波那契数
            int r = fbnq(n);
            String response = String.valueOf(r);

            //设置发回响应的id, 与请求id一致, 这样客户端可以把该响应与它的请求进行对应
            BasicProperties replyProps = new BasicProperties.Builder()
                    .correlationId(message.getProperties().getCorrelationId())
                    .build();

            /*
             * 发送响应消息
             * 1. 默认交换机
             * 2. 由客户端指定的,用来传递响应消息的队列名
             * 3. 参数(关联id)
             * 4. 发回的响应消息
             */
            channel.basicPublish("", message.getProperties().getReplyTo(), replyProps, response.getBytes(StandardCharsets.UTF_8));
            //发送确认消息
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };
        CancelCallback cancelCallback = consumerTag -> System.out.println("consumerTag: " + consumerTag);

        //消费者开始接收消息, 等待从 rpc_queue接收请求消息, 不自动确认
        channel.basicConsume("rpc_queue", false, deliverCallback, cancelCallback);
    }

    protected static int fbnq(int n) {
        if (n == 1 || n == 2) return 1;
        return fbnq(n - 1) + fbnq(n - 2);
    }
}
