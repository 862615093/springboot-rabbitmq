package com.ww.mq.consumer;

import com.rabbitmq.client.Channel;
import com.ww.mq.config.RabbitMqConfig1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class MessageReceiver {


    @RabbitHandler
    @RabbitListener(queues = RabbitMqConfig1.DMP_QUEUE)
    public void onMessage2(Message message, Channel channel) throws IOException {
        // 消息投递的标签号，在同一个channel内按顺序递增
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        // 参数1：消息标签号  参数2：是否批量确认。一般为false，处理1个确认1个；否则会一次性确认掉当前消息标签号之前的全部消息，可能造成丢失
        channel.basicAck(deliveryTag, false);
        System.out.println("确认了消息：" + deliveryTag);
        log.info("使用延迟插件，收到消息:{}", new String(message.getBody()));
    }
}