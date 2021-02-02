package com.demo.rabbitmq.路由模式.producer;

import com.demo.rabbitmq.路由模式.common.CommonField;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Channel channel = CommonField.getChannel();

        channel.exchangeDeclare("logs_direct", BuiltinExchangeType.DIRECT);

        String[] logLevel = {"info", "warning", "error"};

        for (int i = 0; ; i++) {
            int index = new Random().nextInt(3);
            String level = logLevel[index];
            String msg = "No. : " + i + " === log-level : " + level;
            channel.basicPublish("logs_direct", level, null, msg.getBytes(StandardCharsets.UTF_8));
            System.out.println("第 " + i + " 条消息发送成功 ==>" + msg);
            Thread.sleep(1000);
        }
    }
}
