package com.ww.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMqConfig {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 添加json格式序列化器
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @PostConstruct
    public void initRabbitTemplate() {
        // 设置发送端确认回调
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             * @param correlationData 当前消息的唯一关联数据(唯一id)
             * @param ack   rabbitmq服务端是否成功收到消息
             * @param cause 失败的原因
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println("correlationData=" + correlationData);
                System.out.println("ack=" + ack);
                System.out.println("cause=" + cause);
            }
        });
    }

    //1.工作队列模式
    //声明队列，同时交给spring
    @Bean(name = "work-queue")
    public Queue queue0() {
        return new Queue("work-queue");
    }

    //2.发布订阅模式
    //声明了队列
    @Bean(name = "queue1")
    public Queue queue() {
        return new Queue("publish-queue1");
    }

    @Bean(name = "queue2")
    public Queue queue2() {
        return new Queue("publish-queue2");
    }

    //广播的交换机
    //声明交换机  Fanout：广播，将消息交给所有绑定到交换机的队列
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("publish-exchange");
    }

    //将队列绑定到交换机
    @Bean
    Binding bindQueue1ToFanoutExchange(@Qualifier("queue1") Queue queue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }

    //将队列绑定到交换机
    @Bean
    Binding bindQueue2ToFanoutExchange(@Qualifier("queue2") Queue queue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }

    //3.routing模式 -路由模式
    //声明了3个队列
    @Bean(name = "r-queue1")
    public Queue rqueue1() {
        return new Queue("routing-queue1");
    }

    @Bean(name = "r-queue2")
    public Queue rqueue2() {
        return new Queue("routing-queue2");
    }

    @Bean(name = "r-queue3")
    public Queue rqueue3() {
        return new Queue("routing-queue3");
    }

    //声明交换机，路由模式 DirectExchange   Direct：定向，把消息交给符合指定routing key 的队列
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("routing-exchange");
    }

    //建立队列与交换机的关系
    @Bean
    public Binding bindQueue1ToDirectExchange(@Qualifier("r-queue1") Queue queue, DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with("info");
    }

    @Bean
    public Binding bindQueue2ToDirectExchange(@Qualifier("r-queue2") Queue queue, DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with("waring");
    }

    @Bean
    public Binding bindQueue3ToDirectExchange(@Qualifier("r-queue3") Queue queue, DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with("error");
    }

    //4.topic 通配符的模式
    //声明队列
    @Bean(name = "topic-queue1")
    public Queue topicQueue1() {
        return new Queue("topic-queue1");
    }

    @Bean(name = "topic-queue2")
    public Queue topicQueue2() {
        return new Queue("topic-queue2");
    }

    @Bean(name = "topic-queue3")
    public Queue topicQueue3() {
        return new Queue("topic-queue3");
    }

    //声明交换机  Topic：通配符，把消息交给符合routing pattern（路由模式） 的队列
    //通配符模式下的交换机
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("topic-exchange");
    }

    @Bean
    public Binding bindQueue1ToTopicExchange(@Qualifier("topic-queue1") Queue queue, TopicExchange topicExchange) {
        //* 代表一个词
        //# 代表零个或者多个词
        return BindingBuilder.bind(queue).to(topicExchange).with("ex.123.123");
    }

    @Bean
    public Binding bindQueue2ToTopicExchange(@Qualifier("topic-queue2") Queue queue, TopicExchange topicExchange) {
        return BindingBuilder.bind(queue).to(topicExchange).with("ex.*");
    }

    @Bean
    public Binding bindQueue3ToTopicExchange(@Qualifier("topic-queue3") Queue queue, TopicExchange topicExchange) {
        return BindingBuilder.bind(queue).to(topicExchange).with("ex.#");
    }

    //5.发送端可靠性测试
    //声明队列
    @Bean(name = "direct-queue")
    public Queue directQueue() {
        return new Queue("direct-queue");
    }

    //声明交换机，路由模式 DirectExchange   Direct：定向，把消息交给符合指定routing key 的队列
    @Bean(name = "directExchange")
    public DirectExchange directExchange1() {
        return new DirectExchange("amq.direct");
    }

    @Bean
    public Binding bindQueueToDirectExchange(@Qualifier("direct-queue") Queue queue, @Qualifier("directExchange") DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with("confirm");
    }

    //6.死信队列

    /**
     * 死信队列
     */
    @Bean
    Queue dlxQueue() {
        return new Queue("dlx_queue", true, false, false);
    }

    /**
     * 死信交换机
     */
    @Bean
    DirectExchange dlxExchange() {
        return new DirectExchange("dlx_exchange", true, false);
    }

    /**
     * 绑定死信队列和死信交换机
     */
    @Bean
    Binding dlxBinding() {
        return BindingBuilder.bind(dlxQueue()).to(dlxExchange()).with("dlx_routing_key");
    }

    /**
     * 普通消息队列 并设置 死信队列和TTL
     */
    @Bean
    Queue javaboyQueue() {
        Map<String, Object> args = new HashMap<>();
        //设置消息过期时间
        args.put("x-message-ttl", 1000 * 10);
        //设置死信交换机
        args.put("x-dead-letter-exchange", "dlx_exchange");
        //设置死信 routing_key
        args.put("x-dead-letter-routing-key", "dlx_routing_key");
        return new Queue("javaboy_queue", true, false, false, args);
    }

    /**
     * 普通交换机
     */
    @Bean
    DirectExchange javaboyExchange() {
        return new DirectExchange("javaboy_exchange", true, false);
    }

    /**
     * 绑定普通队列和与之对应的交换机
     */
    @Bean
    Binding javaboyBinding() {
        return BindingBuilder.bind(javaboyQueue())
                .to(javaboyExchange())
                .with("javaboy_routing_key");
    }
}
