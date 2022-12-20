package com.ww.mq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DlxConsumer {

    @RabbitListener(queues = "dlx_queue")
    public void handle(Message message, String msg, Channel channel) throws IOException {
        // 消息投递的标签号，在同一个channel内按顺序递增
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        System.out.println("deliveryTag:" + deliveryTag + ">>>>>>" + msg);
        System.out.println("死信队列消费者：" + msg);
        channel.basicAck(deliveryTag, false);
    }
}