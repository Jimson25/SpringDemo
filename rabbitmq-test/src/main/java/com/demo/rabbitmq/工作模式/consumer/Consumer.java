package com.demo.rabbitmq.工作模式.consumer;

import com.demo.rabbitmq.工作模式.common.CommonField;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
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
//        channel.queueDeclare("Operating mode", true, false, false, null);

        //设置该消费者一次只接收一条消息
        channel.basicQos(1);

        DeliverCallback callback = (consumerTag, message) -> {
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);
            //这里当确认接收的消息是`Q`的时候就结束程序运行，那么Q这一条消息会被生产者回收然后发送给其他的消费者处理
            //我们可以等这个消费者结束后在重新启动一个没有做校验的消费者，如果新的消费者接收到了Q这一条消息，就说明消息确认机制工作正常
//            if ("Q".equals(msg)) {
//                System.exit(0);
//            }
            System.out.println("consumerTag: " + consumerTag + " == 收到消息： " + msg);
            //消息确认，false表示只向中间件确认这一条消息已经处理完毕
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };

        //当一个消费者断开连接后的回调
        CancelCallback cancel = consumerTag -> System.out.println("CancelCallback-consumerTag::" + consumerTag);

        //这才开启一个消费者，连接到 ‘Operating mode’这个队列
        //把自动消息确认关闭掉 autoAck = false表示关掉自动确认
        channel.basicConsume("Operating mode", false, callback, cancel);

    }
}
