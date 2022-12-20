package com.ww.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    //1.工作队列模式
    //声明队列，同时交给spring
    @Bean(name = "work-queue")
    public Queue queue0() {
        return new Queue("work-queue");
    }

    //2.发布订阅模式
    //声明了队列
    @Bean(name = "queue1")
    public Queue queue(){
        return new Queue("publish-queue1");
    }

    @Bean(name = "queue2")
    public Queue queue2(){
        return new Queue("publish-queue2");
    }
    //广播的交换机
    //声明交换机  Fanout：广播，将消息交给所有绑定到交换机的队列
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange("publish-exchange");
    }
    //将队列绑定到交换机
    @Bean
    Binding bindQueue1ToFanoutExchange(@Qualifier("queue1")Queue queue, FanoutExchange  fanoutExchange){
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }
    //将队列绑定到交换机
    @Bean
    Binding bindQueue2ToFanoutExchange(@Qualifier("queue2")Queue queue,FanoutExchange  fanoutExchange){
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }

    //3.routing模式 -路由模式
    //声明了3个队列
    @Bean(name = "r-queue1")
    public Queue rqueue1(){
        return new Queue("routing-queue1");
    }
    @Bean(name = "r-queue2")
    public Queue rqueue2(){
        return new Queue("routing-queue2");
    }
    @Bean(name = "r-queue3")
    public Queue rqueue3(){
        return new Queue("routing-queue3");
    }
    //声明交换机，路由模式 DirectExchange   Direct：定向，把消息交给符合指定routing key 的队列
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("routing-exchange");
    }
    //建立队列与交换机的关系
    @Bean
    public Binding bindQueue1ToDirectExchange(@Qualifier("r-queue1")Queue queue,DirectExchange directExchange){
        return BindingBuilder.bind(queue).to(directExchange).with("info");
    }
    @Bean
    public Binding bindQueue2ToDirectExchange(@Qualifier("r-queue2")Queue queue,DirectExchange directExchange){
        return BindingBuilder.bind(queue).to(directExchange).with("waring");
    }
    @Bean
    public Binding bindQueue3ToDirectExchange(@Qualifier("r-queue3")Queue queue,DirectExchange directExchange){
        return BindingBuilder.bind(queue).to(directExchange).with("error");
    }

    //4.topic 通配符的模式
    //声明队列
    @Bean(name = "topic-queue1")
    public Queue topicQueue1(){
        return new Queue("topic-queue1");
    }
    @Bean(name = "topic-queue2")
    public Queue topicQueue2(){
        return new Queue("topic-queue2");
    }
    @Bean(name = "topic-queue3")
    public Queue topicQueue3(){
        return new Queue("topic-queue3");
    }
    //声明交换机  Topic：通配符，把消息交给符合routing pattern（路由模式） 的队列
    //通配符模式下的交换机
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange("topic-exchange");
    }

    @Bean
    public Binding bindQueue1ToTopicExchange(@Qualifier("topic-queue1")Queue queue,TopicExchange topicExchange){
        //* 代表一个词
        //# 代表零个或者多个词
        return BindingBuilder.bind(queue).to(topicExchange).with("ex.123.123");
    }
    @Bean
    public Binding bindQueue2ToTopicExchange(@Qualifier("topic-queue2")Queue queue,TopicExchange topicExchange){
        return BindingBuilder.bind(queue).to(topicExchange).with("ex.*");
    }
    @Bean
    public Binding bindQueue3ToTopicExchange(@Qualifier("topic-queue3")Queue queue,TopicExchange topicExchange){
        return BindingBuilder.bind(queue).to(topicExchange).with("ex.#");
    }
}
