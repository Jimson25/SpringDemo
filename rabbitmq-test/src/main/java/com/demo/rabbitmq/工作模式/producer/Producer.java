package com.demo.rabbitmq.工作模式.producer;

import com.demo.rabbitmq.工作模式.common.CommonField;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * rabbitmq 简单模式 生产者
 */
public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //创建连接工厂并设置连接信息
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(CommonField.RABBIT_MQ_HOST);
        factory.setPort(CommonField.RABBIT_MQ_PORT);
        factory.setUsername(CommonField.RABBIT_MQ_USERNAME);
        factory.setPassword(CommonField.RABBIT_MQ_PASSWORD);

        //rabbitmq使用的是nio，
        Connection connection = factory.newConnection();
        //建立信道
        Channel channel = connection.createChannel();
        /*
         * 在rabbitmq中声明一个队列
         *  - queue         队列名称
         *  - durable       是否持久化队列
         *  - exclusive     排他，表示这个队列只有当前连接可用
         *  - autoDelete    当最后一个消费者断开后是否删除队列
         *  - arguments     其他参数
         */
        channel.queueDeclare("Operating mode", true, false, false, null);

        for (int i = 0; ; i++) {
            String msg = "这是第" + i + "条消息";
//            if (i == 50) {
//                msg = "Q";
//            }
            //发送消息
            channel.basicPublish("", "Operating mode",
                    MessageProperties.PERSISTENT_TEXT_PLAIN, //设置消息持久化
                    msg.getBytes(StandardCharsets.UTF_8));
            System.out.println("第" + i + "条消息已发送");
            Thread.sleep(1000);
        }
//        channel.close();
    }
}
