package com.ww.mq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Producer {

    //rabbitmq跟springboot整合，springboot提供了模板给我们使用。
    //例如：restTemplate  thymeleafTemplate
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //1.工作队列模式
    @Test
    public void testWorkQueue() {
        //使用convertAndSend
        //1.当前队列的名称。2.你要携带的信息内容
        rabbitTemplate.convertAndSend("work-queue", "这是qq一条消息！！");
    }

    //2.广播订阅模式
    @Test
    public void testSendPublish() {
        Map map = new HashMap<>();
        map.put("name", "张三");
        map.put("age", 18);
        //1.交换机的名称  2.你的规则，发布订阅模式为空 3.消息的主题
        rabbitTemplate.convertAndSend("publish-exchange", "", map);
    }

    //3.路由routing模式
    @Test
    public void testRoutingSend() {
        Map map = new HashMap<>();
        map.put("name", "张三");
        map.put("age", 18);
        rabbitTemplate.convertAndSend("routing-exchange", "info", map);
        rabbitTemplate.convertAndSend("routing-exchange", "error", map);
        System.out.println("路由routing模式消息发送成功~");
    }

    //4.topic 通配符的模式
    @Test
    public void testTopicSend() {
        Map map = new HashMap<>();
        map.put("name", "张三");
        map.put("age", 20);
        rabbitTemplate.convertAndSend("topic-exchange", "ex.123.1238", map);
        System.out.println("通配符的模式消息发送成功~");
    }
}
