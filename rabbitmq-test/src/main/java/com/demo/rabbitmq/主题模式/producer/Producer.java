package com.demo.rabbitmq.主题模式.producer;

import com.demo.rabbitmq.主题模式.common.Common;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Channel channel = Common.getChannel();

        //声明一个Topic类型的交换机，命名为 car_exchange
        channel.exchangeDeclare("phone_exchange", BuiltinExchangeType.TOPIC);

        //<品牌>.<颜色>.<大小>
        String[] brands = {"华为", "小米", "vivo", "oppo"};
        String[] colors = {"black", "white", "blue"};
        String[] sizes = {"big", "small"};

        for (int i = 0; ; i++) {
            int brandIndex = new Random().nextInt(4);
            int colorIndex = new Random().nextInt(3);
            int sizeIndex = new Random().nextInt(2);

            String routingKey = brands[brandIndex] + "." + colors[colorIndex] + "." + sizes[sizeIndex];
            String msg = "index: " + i + " == routing-key:" + routingKey;
            System.out.println("第 " + i + " 条消息已发送 === " + msg);
            channel.basicPublish("phone_exchange", routingKey, null, msg.getBytes(StandardCharsets.UTF_8));
            Thread.sleep(1000);
        }
    }

}
