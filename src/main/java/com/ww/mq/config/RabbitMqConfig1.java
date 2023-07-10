package com.ww.mq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试延迟插件消息队列
 */
@Configuration
@Slf4j
public class RabbitMqConfig1 {


    //延时插件DelayedMessagePlugin的交换机，队列，路由相关配置
    public static final String DMP_EXCHANGE = "dmp.exchange";
    public static final String DMP_ROUTEKEY = "dmp.routeKey";
    public static final String DMP_QUEUE = "dmp.queue";


//    @PostConstruct
//    public void initRabbitTemplate()  {
//        //设置发送端交换机收到消息 确认回调 (正常和异常 都会回调)
//        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
//            /**
//             * @param correlationData 当前消息的唯一关联数据(唯一id)
//             * @param ack   rabbitmq服务端是否成功收到消息
//             * @param cause 失败的原因
//             */
//            @Override
//            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
//                System.out.println("ConfirmCallback------------------------------------------>");
//                // 判断结果
//                if (ack) {
//                    // ACK
//                    log.info("消息成功投递到交换机！消息ID: {}", correlationData.getId());
//                } else {
//                    // NACK
//                    log.error("消息投递到交换机失败！消息ID：{}", correlationData.getId());
//                    // 重发消息
//                }
//                System.out.println("correlationData=" + correlationData);
//                System.out.println("ack=" + ack);
//                System.out.println("cause=" + cause);
//            }
//        });
//
//        //设置发送端消息队列收到消息 确认回调 (消息队列没收到 才会回调)
//        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
//            /**
//             * 只要消息没有正确投递给指定的队列，就会触发这个失败回调
//             * @param message  投递失败的消息的详细信息
//             * @param replyCode 回复的状态码
//             * @param replyText 回复的文本内容
//             * @param exchange  这个消息发送给哪个交换机
//             * @param routingKey 发送这个消息使用的路由键
//             */
//            @Override
//            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
//                System.out.println("ReturnCallback------------------------------------------>");
//                System.out.println("交换机投递到消息队列失败！");
//                System.out.println("message:" + message);
//                System.out.println("replyCode:" + replyCode);
//                System.out.println("replyText:" + replyText);
//                System.out.println("exchange:" + exchange);
//                System.out.println("routingKey:" + routingKey);
//            }
//        });
//    }


    //延迟插件使用
    //1、声明一个类型为x-delayed-message的交换机
    //2、参数添加一个x-delayed-type值为交换机的类型用于路由key的映射
    @Bean
    public CustomExchange dmpExchange() {
        Map<String, Object> arguments = new HashMap<>(1);
        arguments.put("x-delayed-type", "direct");
        return new CustomExchange(DMP_EXCHANGE, "x-delayed-message", true, false, arguments);
    }

    @Bean
    public Queue dmpQueue() {
        return new Queue(DMP_QUEUE, true, false, false);
    }

    @Bean
    public Binding dmpBind() {
        return BindingBuilder.bind(dmpQueue()).to(dmpExchange()).with(DMP_ROUTEKEY).noargs();
    }
}
