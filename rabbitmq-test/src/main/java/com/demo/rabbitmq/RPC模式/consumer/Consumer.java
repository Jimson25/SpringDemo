package com.demo.rabbitmq.RPC模式.consumer;

import com.demo.rabbitmq.RPC模式.common.Common;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Consumer {

    public String call(String msg) throws Exception {

        Channel channel = Common.getChannel();

        //生成一个随机队列
        String queue = channel.queueDeclare().getQueue();
        //生成随机id
        String uuid = UUID.randomUUID().toString();

        //设置请求和响应的关联id
        //传递相应数据的queue
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .correlationId(uuid)
                .replyTo(queue)
                .build();
        //向队列发送请求
        channel.basicPublish("", "rpc_queue", properties, msg.getBytes(StandardCharsets.UTF_8));

        //用来保存结果的阻塞集合,取数据时,没有数据会暂停等待
        BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

        //接收响应数据的回调对象
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            //如果响应消息的关联id,与请求的关联id相同,我们来处理这个响应数据
            if (message.getProperties().getCorrelationId().contentEquals(uuid)) {
                //把收到的响应数据,放入阻塞集合
                response.offer(new String(message.getBody(), StandardCharsets.UTF_8));
            }
        };

        CancelCallback cancelCallback = consumerTag -> System.out.printf("consumerTag: %s%n", consumerTag);

        //开始从队列接收响应数据
        channel.basicConsume(queue, true, deliverCallback, cancelCallback);
        //返回保存在集合中的响应数据
        return response.take();
    }

    public static void main(String[] args) throws Exception {
        Consumer client = new Consumer();
        while (true) {
            System.out.print("求第几个斐波那契数:");
            int n = new Scanner(System.in).nextInt();
            String r = client.call("" + n);
            System.out.println(r);
        }
    }
}

