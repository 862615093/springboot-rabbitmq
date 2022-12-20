package com.ww.mq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class Consumer {

    //1.工作队列模式
    @RabbitListener(queues = "work-queue")
    public void workQueue(String str) {
        System.out.println("工作队列模式当前监听到了：" + str);
    }

    //2.广播订阅模式
    @RabbitListener(queues = "publish-queue1")
    public void publishQueue(Map str) {
        System.out.println("publish-queue1 当前监听到了：" + str);
    }

    @RabbitListener(queues = "publish-queue2")
    public void publishQueue2(Map str) {
        System.out.println("publish-queue2 当前监听到了：" + str);
    }

    //3.routing模式-路由模式
    @RabbitListener(queues = "routing-queue1")
    public void routingQueue1(Map str) {
        System.out.println("路由模式监听到了info消息" + str);
    }

    @RabbitListener(queues = "routing-queue2")
    public void routingQueue2(Map str) {
        System.out.println("路由模式监听到了warning消息" + str);
    }

    @RabbitListener(queues = "routing-queue3")
    public void routingQueue3(Map str) {
        System.out.println("路由模式监听到了error消息" + str);
    }

    //4.topic 通配符的模式
    @RabbitListener(queues = "topic-queue1")
    public void topicQueue1(Map str) {
        System.out.println("通配符的模式监听到了info消息" + str);
    }

    @RabbitListener(queues = "topic-queue2")
    public void topicQueue2(Map str) {
        System.out.println("通配符的模式监听到了警告消息" + str);
    }

    @RabbitListener(queues = "topic-queue3")
    public void topicQueue3(Map str) {
        System.out.println("通配符的模式监听到了错误消息" + str);
    }

    //消费
    @RabbitListener(queues = "confirm-queue")
    public void confirmQueue(Object str, Channel channel, Message message) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        channel.basicAck(deliveryTag, false);
        System.out.println("confirm-queue=" + str);
    }
}
